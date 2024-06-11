
package jigl.image.ops.levelOps;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    // Test apply(GrayImage image)

    @Test
    public void testApplyWithGrayImage() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 3);

        Image result = clip.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }

    // Test apply(GrayImage image, ROI roi)

    @Test
    public void testApplyWithGrayImageAndROI() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 3);

        ROI roi = new ROI(0, 0, 2, 2);
        Image result = clip.apply(image, roi);

        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }

    // Test apply(RealGrayImage image)

    @Test
    public void testApplyWithRealGrayImage() {
        Clip clip = new Clip(0, 0);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.5f);
        image.set(1, 1, 2.5f);
        image.set(2, 2, 3.5f);

        Image result = clip.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }

    // Test apply(RealGrayImage image, ROI roi)

    @Test
    public void testApplyWithRealGrayImageAndROI() {
        Clip clip = new Clip(0, 0);
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.5f);
        image.set(1, 1, 2.5f);
        image.set(2, 2, 3.5f);

        ROI roi = new ROI(0, 0, 2, 2);
        Image result = clip.apply(image, roi);

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }

    // Test apply(ColorImage image)

    @Test
    public void testApplyWithColorImage() {
        Clip clip = new Clip(0, 0);
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 0, 10);
        image.set(1, 1, 1, 5);
        image.set(2, 2, 2, 3);

        Image result = clip.apply(image);

        assertEquals(0, result.get(0, 0, 0));
        assertEquals(5, result.get(1, 1, 1));
        assertEquals(3, result.get(2, 2, 2));
    }

    // Test apply(ColorImage image, ROI roi)

    @Test
    public void testApplyWithColorImageAndROI() {
        Clip clip = new Clip(0, 0);
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 0, 10);
        image.set(1, 1, 1, 5);
        image.set(2, 2, 2, 3);

        ROI roi = new ROI(0, 0, 2, 2);
        Image result = clip.apply(image, roi);

        assertEquals(0, result.get(0, 0, 0));
        assertEquals(5, result.get(1, 1, 1));
        assertEquals(3, result.get(2, 2, 2));
    }

    // Test apply(RealColorImage image)

    @Test
    public void testApplyWithRealColorImage() {
        Clip clip = new Clip(0, 0);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 0, 1.5f);
        image.set(1, 1, 1, 2.5f);
        image.set(2, 2, 2, 3.5f);

        Image result = clip.apply(image);

        assertEquals(1, result.get(0, 0, 0));
        assertEquals(2, result.get(1, 1, 1));
        assertEquals(3, result.get(2, 2, 2));
    }

    // Test apply(RealColorImage image, ROI roi)

    @Test
    public void testApplyWithRealColorImageAndROI() {
        Clip clip = new Clip(0, 0);
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, 0, 1.5f);
        image.set(1, 1, 1, 2.5f);
        image.set(2, 2, 2, 3.5f);

        ROI roi = new ROI(0, 0, 2, 2);
        Image result = clip.apply(image, roi);

        assertEquals(1, result.get(0, 0, 0));
        assertEquals(2, result.get(1, 1, 1));
        assertEquals(3, result.get(2, 2, 2));
    }

}
