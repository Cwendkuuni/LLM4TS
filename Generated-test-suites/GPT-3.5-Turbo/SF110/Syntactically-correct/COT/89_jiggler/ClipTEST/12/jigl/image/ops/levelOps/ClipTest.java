
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.RegionOfInterest;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        // Test case 1
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(0, 255);
        GrayImage result = (GrayImage) clip.apply(image);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 100);
        clip = new Clip(50, 200);
        result = (GrayImage) clip.apply(image);
        assertEquals(100, result.get(5, 5));

        // Test case 3
        clip = new Clip(-100, 500);
        result = (GrayImage) clip.apply(image);
        assertEquals(0, result.get(5, 5));
    }

    @Test
    public void testApplyGrayImageROI() {
        // Test case 1
        GrayImage image = new GrayImage(10, 10);
        RegionOfInterest roi = new RegionOfInterest(0, 0, 5, 5);
        Clip clip = new Clip(0, 255);
        GrayImage result = (GrayImage) clip.apply(image, roi);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 100);
        clip = new Clip(50, 200);
        result = (GrayImage) clip.apply(image, roi);
        assertEquals(100, result.get(5, 5));

        // Test case 3
        clip = new Clip(-100, 500);
        result = (GrayImage) clip.apply(image, roi);
        assertEquals(0, result.get(5, 5));
    }

    @Test
    public void testApplyRealGrayImage() {
        // Test case 1
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage result = (RealGrayImage) clip.apply(image);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 0.5f);
        clip = new Clip(0.2f, 0.8f);
        result = (RealGrayImage) clip.apply(image);
        assertEquals(0.5f, result.get(5, 5), 0.001f);

        // Test case 3
        clip = new Clip(-1.0f, 2.0f);
        result = (RealGrayImage) clip.apply(image);
        assertEquals(0.0f, result.get(5, 5), 0.001f);
    }

    @Test
    public void testApplyRealGrayImageROI() {
        // Test case 1
        RealGrayImage image = new RealGrayImage(10, 10);
        RegionOfInterest roi = new RegionOfInterest(0, 0, 5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage result = (RealGrayImage) clip.apply(image, roi);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 0.5f);
        clip = new Clip(0.2f, 0.8f);
        result = (RealGrayImage) clip.apply(image, roi);
        assertEquals(0.5f, result.get(5, 5), 0.001f);

        // Test case 3
        clip = new Clip(-1.0f, 2.0f);
        result = (RealGrayImage) clip.apply(image, roi);
        assertEquals(0.0f, result.get(5, 5), 0.001f);
    }

    @Test
    public void testApplyColorImage() {
        // Test case 1
        ColorImage image = new ColorImage(10, 10);
        Clip clip = new Clip(0, 255);
        ColorImage result = (ColorImage) clip.apply(image);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 100);
        clip = new Clip(50, 200);
        result = (ColorImage) clip.apply(image);
        assertEquals(100, result.plane(0).get(5, 5));

        // Test case 3
        clip = new Clip(-100, 500);
        result = (ColorImage) clip.apply(image);
        assertEquals(0, result.plane(0).get(5, 5));
    }

    @Test
    public void testApplyColorImageROI() {
        // Test case 1
        ColorImage image = new ColorImage(10, 10);
        RegionOfInterest roi = new RegionOfInterest(0, 0, 5, 5);
        Clip clip = new Clip(0, 255);
        ColorImage result = (ColorImage) clip.apply(image, roi);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 100);
        clip = new Clip(50, 200);
        result = (ColorImage) clip.apply(image, roi);
        assertEquals(100, result.plane(0).get(5, 5));

        // Test case 3
        clip = new Clip(-100, 500);
        result = (ColorImage) clip.apply(image, roi);
        assertEquals(0, result.plane(0).get(5, 5));
    }

    @Test
    public void testApplyRealColorImage() {
        // Test case 1
        RealColorImage image = new RealColorImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage result = (RealColorImage) clip.apply(image);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 0.5f);
        clip = new Clip(0.2f, 0.8f);
        result = (RealColorImage) clip.apply(image);
        assertEquals(0.5f, result.plane(0).get(5, 5), 0.001f);

        // Test case 3
        clip = new Clip(-1.0f, 2.0f);
        result = (RealColorImage) clip.apply(image);
        assertEquals(0.0f, result.plane(0).get(5, 5), 0.001f);
    }

    @Test
    public void testApplyRealColorImageROI() {
        // Test case 1
        RealColorImage image = new RealColorImage(10, 10);
        RegionOfInterest roi = new RegionOfInterest(0, 0, 5, 5);
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage result = (RealColorImage) clip.apply(image, roi);
        assertEquals(image, result);

        // Test case 2
        image.set(5, 5, 0.5f);
        clip = new Clip(0.2f, 0.8f);
        result = (RealColorImage) clip.apply(image, roi);
        assertEquals(0.5f, result.plane(0).get(5, 5), 0.001f);

        // Test case 3
        clip = new Clip(-1.0f, 2.0f);
        result = (RealColorImage) clip.apply(image, roi);
        assertEquals(0.0f, result.plane(0).get(5, 5), 0.001f);
    }
    
    @Test
    public void testMain() {
        String[] argv = new String[4];
        argv[0] = "0.2";
        argv[1] = "0.8";
        argv[2] = "input.jpg";
        argv[3] = "output.jpg";
        
        Clip.main(argv);
        
        // TODO: Assert the correctness of the output image
    }
}

