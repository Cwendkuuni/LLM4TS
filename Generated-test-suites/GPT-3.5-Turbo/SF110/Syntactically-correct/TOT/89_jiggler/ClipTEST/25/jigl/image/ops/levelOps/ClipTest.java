import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }

    @Test
    public void testApply_GrayImage_AllValuesClipped() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        clip.apply(image);
        
        assertEquals(0, image.get(0, 0));
        assertEquals(200, image.get(1, 1));
        assertEquals(255, image.get(2, 2));
    }

    @Test
    public void testApply_GrayImage_ROI_AllValuesClipped() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);

        ROI roi = new ROI(1, 1, 2, 2);
        clip.apply(image, roi);
        
        assertEquals(100, image.get(0, 0));
        assertEquals(200, image.get(1, 1));
        assertEquals(255, image.get(2, 2));
    }

    @Test
    public void testApply_RealGrayImage_AllValuesClipped() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.1f);
        image.set(1, 1, 0.5f);
        image.set(2, 2, 0.9f);
        clip.apply(image);
        
        assertEquals(0, image.get(0, 0), 0.001);
        assertEquals(0.5f, image.get(1, 1), 0.001);
        assertEquals(0.9f, image.get(2, 2), 0.001);
    }

    @Test
    public void testApply_RealGrayImage_ROI_AllValuesClipped() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.1f);
        image.set(1, 1, 0.5f);
        image.set(2, 2, 0.9f);
        
        ROI roi = new ROI(1, 1, 2, 2);
        clip.apply(image, roi);
        
        assertEquals(0.1f, image.get(0, 0), 0.001);
        assertEquals(0.5f, image.get(1, 1), 0.001);
        assertEquals(0.9f, image.get(2, 2), 0.001);
    }

    @Test
    public void testApply_ColorImage_AllValuesClipped() {
        ColorImage image = new ColorImage(3, 3);
        image.setRGB(0, 0, 100, 100, 100);
        image.setRGB(1, 1, 200, 200, 200);
        image.setRGB(2, 2, 300, 300, 300);
        clip.apply(image);
        
        assertEquals(0, image.getPlane(0).get(0, 0));
        assertEquals(200, image.getPlane(0).get(1, 1));
        assertEquals(255, image.getPlane(0).get(2, 2));
    }

    @Test
    public void testApply_ColorImage_ROI_AllValuesClipped() {
        ColorImage image = new ColorImage(3, 3);
        image.setRGB(0, 0, 100, 100, 100);
        image.setRGB(1, 1, 200, 200, 200);
        image.setRGB(2, 2, 300, 300, 300);
        
        ROI roi = new ROI(1, 1, 2, 2);
        clip.apply(image, roi);
        
        assertEquals(100, image.getPlane(0).get(0, 0));
        assertEquals(200, image.getPlane(0).get(1, 1));
        assertEquals(255, image.getPlane(0).get(2, 2));
    }

    @Test
    public void testApply_RealColorImage_AllValuesClipped() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setRGB(0, 0, 0.1f, 0.1f, 0.1f);
        image.setRGB(1, 1, 0.5f, 0.5f, 0.5f);
        image.setRGB(2, 2, 0.9f, 0.9f, 0.9f);
        clip.apply(image);
        
        assertEquals(0, image.getPlane(0).get(0, 0), 0.001);
        assertEquals(0.5f, image.getPlane(0).get(1, 1), 0.001);
        assertEquals(0.9f, image.getPlane(0).get(2, 2), 0.001);
    }

    @Test
    public void testApply_RealColorImage_ROI_AllValuesClipped() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setRGB(0, 0, 0.1f, 0.1f, 0.1f);
        image.setRGB(1, 1, 0.5f, 0.5f, 0.5f);
        image.setRGB(2, 2, 0.9f, 0.9f, 0.9f);
        
        ROI roi = new ROI(1, 1, 2, 2);
        clip.apply(image, roi);
        
        assertEquals(0.1f, image.getPlane(0).get(0, 0), 0.001);
        assertEquals(0.5f, image.getPlane(0).get(1, 1), 0.001);
        assertEquals(0.9f, image.getPlane(0).get(2, 2), 0.001);
    }
}