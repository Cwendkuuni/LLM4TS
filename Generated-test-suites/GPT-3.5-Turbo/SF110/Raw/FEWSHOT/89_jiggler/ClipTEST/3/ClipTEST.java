
package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {
    @Test
    public void testApplyGrayImageDefault() {
        Clip clip = new Clip(0, 0);
        GrayImage image = mock(GrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyGrayImageWithROI() {
        Clip clip = new Clip(0, 0);
        GrayImage image = mock(GrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image, roi);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyRealGrayImageDefault() {
        Clip clip = new Clip(0, 0);
        RealGrayImage image = mock(RealGrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyRealGrayImageWithROI() {
        Clip clip = new Clip(0, 0);
        RealGrayImage image = mock(RealGrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image, roi);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyColorImageDefault() {
        Clip clip = new Clip(0, 0);
        ColorImage image = mock(ColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyColorImageWithROI() {
        Clip clip = new Clip(0, 0);
        ColorImage image = mock(ColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image, roi);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyRealColorImageDefault() {
        Clip clip = new Clip(0, 0);
        RealColorImage image = mock(RealColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image);
        
        //assertions
        //...
    }
    
    @Test
    public void testApplyRealColorImageWithROI() {
        Clip clip = new Clip(0, 0);
        RealColorImage image = mock(RealColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        
        clip.apply(image, roi);
        
        //assertions
        //...
    }
    
    @Test
    public void testMain() {
        //create test input file
        //...
        
        Clip clip = new Clip(0, 0);
        String[] argv = {"0.0", "0.0", "inputfile", "outputfile"};
        
        clip.main(argv);
        
        //assertions
        //...
    }
}
