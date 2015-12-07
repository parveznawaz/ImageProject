package org.parvez.Image;



	import java.awt.*;
	import javax.swing.*;
	import java.awt.image.*;

	public class ImageManipulator {

	    /* First, some utility methods that you will need in the methods you write.
	       Do not modify these methods in any way. */

	    public static int getRed(int rgb) { return (rgb >> 16) & 0xff; }
	    public static int getGreen(int rgb) { return (rgb >> 8) & 0xff; }
	    public static int getBlue(int rgb) { return rgb & 0xff; }
	    public static int rgbColour(int r, int g, int b) {
	        return (r << 16) | (g << 8) | b;
	    }
	    public static double brightness(int rgb) {
	        int r = getRed(rgb);
	        int g = getGreen(rgb);
	        int b = getBlue(rgb);
	        return 0.21*r + 0.72*g + 0.07*b;
	    }

	    public static BufferedImage convertToGrayscale(BufferedImage img) {
	        BufferedImage result = new BufferedImage(
	                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB
	            );
	        for(int x = 0; x < img.getWidth(); x++) {
	            for(int y = 0; y < img.getHeight(); y++) {
	                int col = img.getRGB(x, y);
	                int gr = (int)brightness(col);
	                result.setRGB(x, y, rgbColour(gr, gr, gr));
	            }
	        }
	        return result;
	    }

	    /* ----------- Methods that you will write in this assignment. */

	    public static BufferedImage thresholdImage(BufferedImage img, int threshold) {
	        // fill this in
	        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	        for(int x = 0; x < img.getWidth(); x++)
	        {
	            for(int y = 0; y < img.getHeight(); y++)
	            {
	                int col = img.getRGB(x, y);
	                int gr = (int)brightness(col);
	                if(gr > threshold)
	                {
	                    result.setRGB(x , y, rgbColour(255, 255, 255));
	                }
	                else
	                {
	                    result.setRGB(x, y, rgbColour(0, 0, 0));
	                }
	            }
	        }
	        
	        return result;
	    }
	    
	    public static BufferedImage horizontalMirror(BufferedImage img) {
	        // fill this in
	        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	        for(int x = 0; x < img.getWidth(); x++)
	        {
	        	for(int y = 0; y < img.getHeight(); y++)
	            {
	                int col = img.getRGB(x, y);                
	                result.setRGB(img.getWidth()-1-x, y, col);
	            }
	            
	        }
	        return result;
	    }

	    public static BufferedImage splitToFour(BufferedImage img) {
	        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	        int factor=2;
	        
	        BufferedImage smallImage=new BufferedImage(img.getWidth()/factor, img.getHeight()/factor, BufferedImage.TYPE_INT_RGB);
	        for(int x=0;x<img.getWidth()/factor;x++)
	        {
	        	for(int y=0;y<img.getHeight()/factor;y++)
	        	{
	        		smallImage.setRGB(x, y, img.getRGB(factor*x, factor*y));
	        	}
	        }
	        
	        for(int x=0;x<img.getWidth();x++)
	        {
	        	for(int y=0;y<img.getHeight();y++)
	        	{
	        		int col=smallImage.getRGB(x%(img.getWidth()/factor), y%(img.getHeight()/factor));
	        		result.setRGB(x, y, col);
	        	}
	        }
	        
	      
	        return result;
	    }

	    public static BufferedImage imageCorrelation(BufferedImage img, double[][] mask) {
	        // fill this in
	    	BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	        
	    	for(int x=0;x<img.getWidth();x++)
	    	{
	    		for(int y=0;y<img.getHeight();y++)
	    		{
	    			if(x==0 || y==0 || (x==img.getWidth()-1)||(y==img.getHeight()-1))
	    			{
	    				result.setRGB(x, y, rgbColour(0, 0, 0));
	    			}
	    			
	    			else
	    			{
	    				int accumulatorRed=0;
	    				int accumulatorGreen=0;
	    				int accumulatorBlue=0;
	    				
	    				
	        			for(int i=0;i<3;i++)
	        			{
	        				for(int j=0;j<3;j++)
	        				{
	        					accumulatorRed+=getRed(img.getRGB(x+i-1, y+i-1))*mask[i][j];
	        					accumulatorGreen+=getGreen(img.getRGB(x+i-1, y+i-1))*mask[i][j];
	        					accumulatorBlue+=getBlue(img.getRGB(x+i-1, y+i-1))*mask[i][j];
	        				}
	        			}
	    				
	        			accumulatorRed=accumulatorRed>255?255:accumulatorRed;
	        			accumulatorGreen=accumulatorGreen>255?255:accumulatorGreen;
	        			accumulatorBlue=accumulatorBlue>255?255:accumulatorBlue;
	        			accumulatorRed=accumulatorRed<0?0:accumulatorRed;
	        			accumulatorGreen=accumulatorGreen<0?0:accumulatorGreen;
	        			accumulatorBlue=accumulatorBlue<0?0:accumulatorBlue;
	        			
	        	
	        			result.setRGB(x, y, rgbColour(accumulatorRed, accumulatorGreen, accumulatorBlue));
	    			}    			
	    			
	    		}
	    	}
	    	
	    	return result;
	    }
	    
	    public static BufferedImage rowPixelSort(BufferedImage img, int n) {
	        // fill this in
	    	
	    	for(int y=0;y<img.getHeight();y++)
	    	{
	    		for(int x=0;x<n;x++)
	        	{    
	    			for(int z=x+1;z<n;z++)
	    			{
	    				int colX = img.getRGB(x, y);
	                    int grX = (int)brightness(colX);
	                    int colZ = img.getRGB(z, y);
	                    int grZ = (int)brightness(colZ);
	                    if(grX>grZ)
	                    {
	                    	img.setRGB(x, y, colZ);
	                    	img.setRGB(z, y, colX);
	                    }
	    			}        	
	        	
	        	}
	    	}
	    	return img;
	    }
	    // ------------------------------------ end of your code

	    /* A utility method we need to convert Image objects to BufferedImage, copied from 
	     * http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
	     */
	    public static BufferedImage toBufferedImage(Image img) {
	        if (img instanceof BufferedImage) { return (BufferedImage) img;}
	        // Create a buffered image with transparency
	        BufferedImage bimage = new BufferedImage(
	                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB
	            );
	        // Draw the image on to the buffered image
	        Graphics2D bGr = bimage.createGraphics();
	        bGr.drawImage(img, 0, 0, null);
	        bGr.dispose();
	        // Return the buffered image
	        return bimage;
	    }

	    /* A utility method to create a JPanel instance that displays the given Image. */
	    public static JPanel createPanel(Image img) {
	        // Define a local class from which we create an object to return as result.
	        class ImagePanel extends JPanel {
				private static final long serialVersionUID = 1L;
					private Image img;
	            public ImagePanel(Image img) {
	                this.img = img;
	                this.setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));               
	            }

	            public void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                g.drawImage(img, 0, 0, this);
	            }
	        }
	        return new ImagePanel(img);
	    }

	    /* The main method to try out the whole shaping. */
	    public static void main(String[] args) {
	        Image img = Toolkit.getDefaultToolkit().getImage("ryerson1.jpg");        
	        MediaTracker m = new MediaTracker(new JPanel());
	        m.addImage(img, 0);
	        try { m.waitForAll(); } catch(InterruptedException e) { }
	        BufferedImage bimg = toBufferedImage(img); 
	        JFrame f = new JFrame("CCPS 109 Lab 7");
	        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        f.setLayout(new GridLayout(2, 3));
	        f.add(createPanel(thresholdImage(bimg, 150)));
	        f.add(createPanel(horizontalMirror(bimg)));
	        f.add(createPanel(splitToFour(bimg)));
	        double wt = 1.0/9;
	        double[][] blur = {{wt,wt,wt},{wt,wt,wt},{wt,wt,wt}};
	        f.add(createPanel(imageCorrelation(bimg, blur)));
	        double[][] edged ={{-1,-1,-1},{-1,8,-1},{-1,-1,-1}};
	        f.add(createPanel(imageCorrelation(convertToGrayscale(bimg), edged)));
	        //double [][] sharpen = {{0,-1,0},{-1,5,-1},{0,-1,0}};
	        //f.add(createPanel(imageCorrelation(bimg, sharpen)));
	        f.add(createPanel(rowPixelSort(bimg, bimg.getWidth())));
	        f.pack();
	        f.setVisible(true); 
	    }
	}

