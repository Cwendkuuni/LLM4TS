import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.types.*;

public class ClipTest {

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(1, 1, 10);
        Clip clip = new Clip(5, 15);
        Image result = clip.apply(image);
        assertEquals(10, result.get(1, 1));
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(1, 1, 10.5f);
        Clip clip = new Clip(5.0f, 15.0f);
        Image result = clip.apply(image);
        assertEquals(10.5f, result.get(1, 1));
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.getPlane(0).set(1, 1, 10);
        Clip clip = new Clip(5, 15);
        Image result = clip.apply(image);
        assertEquals(10, ((GrayImage)result.getPlane(0)).get(1, 1));
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.getPlane(0).set(1, 1, 10.5f);
        Clip clip = new Clip(5.0f, 15.0f);
        Image result = clip.apply(image);
        assertEquals(10.5f, ((RealGrayImage)result.getPlane(0)).get(1, 1));
    }

    @Test
    public void testApplyException() {
        GrayImage image = new GrayImage(3, 3);
        try {
            Clip clip = new Clip(5, 15);
            clip.apply(image, new ROI(0, 0, 4, 4)); // ROI out of image bounds
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
}