import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.io.*;

public class ClipTest {
    
    @Test
    public void testApplyWithIntMinMax() {
        Clip clip = new Clip(10, 100);
        GrayImage image = new GrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApplyWithFloatMinMax() {
        Clip clip = new Clip(0.5f, 0.8f);
        RealGrayImage image = new RealGrayImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApplyWithColorImageIntMinMax() {
        Clip clip = new Clip(10, 100);
        ColorImage image = new ColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testApplyWithColorImageFloatMinMax() {
        Clip clip = new Clip(0.5f, 0.8f);
        RealColorImage image = new RealColorImage();
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Image result = clip.apply(image, roi);
        assertNotNull(result);
    }
    
    @Test
    public void testMain() {
        String[] argv = new String[]{"0.5", "0.8", "inputfile", "outputfile"};
        Clip.main(argv);
    }
}