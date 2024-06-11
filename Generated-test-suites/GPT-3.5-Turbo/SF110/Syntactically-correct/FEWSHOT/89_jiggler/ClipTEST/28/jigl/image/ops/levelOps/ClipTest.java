
package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import org.junit.Test;

public class ClipTest {
    
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(0, 255);
        GrayImage result = clip.apply(image);
        
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_GrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(0, 255);
        GrayImage result = clip.apply(image, roi);
        
        assertEquals(roi.lx() - roi.ux() + 1, result.X());
        assertEquals(roi.ly() - roi.uy() + 1, result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage result = clip.apply(image);
        
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage result = clip.apply(image, roi);
        
        assertEquals(roi.lx() - roi.ux() + 1, result.X());
        assertEquals(roi.ly() - roi.uy() + 1, result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(10, 10);
        Clip clip = new Clip(0, 255);
        ColorImage result = clip.apply(image);
        
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_ColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(0, 255);
        ColorImage result = clip.apply(image, roi);
        
        assertEquals(roi.lx() - roi.ux() + 1, result.X());
        assertEquals(roi.ly() - roi.uy() + 1, result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage result = clip.apply(image);
        
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testApply_RealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage result = clip.apply(image, roi);
        
        assertEquals(roi.lx() - roi.ux() + 1, result.X());
        assertEquals(roi.ly() - roi.uy() + 1, result.Y());
        assertNotNull(result);
    }
    
    @Test
    public void testMain() {
        String[] argv = new String[]{"0.0", "1.0", "inputfile", "outputfile"};
        Clip.main(argv);
        // Add assertions for the generated output file
        
        // Cleanup the generated output file after assertions
    }
}
