package jigl.image.ops.levelOps;

import org.junit.Test;
import org.junit.Assert;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        // Create test data
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(0, 255);

        // Apply the method
        Image result = clip.apply(image);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_GrayImage_ROI() {
        // Create test data
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0, 255);

        // Apply the method
        Image result = clip.apply(image, roi);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_RealGrayImage() {
        // Create test data
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);

        // Apply the method
        Image result = clip.apply(image);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_RealGrayImage_ROI() {
        // Create test data
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.0f, 1.0f);

        // Apply the method
        Image result = clip.apply(image, roi);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_ColorImage() {
        // Create test data
        ColorImage image = new ColorImage(10, 10);
        Clip clip = new Clip(0, 255);

        // Apply the method
        Image result = clip.apply(image);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_ColorImage_ROI() {
        // Create test data
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0, 255);

        // Apply the method
        Image result = clip.apply(image, roi);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_RealColorImage() {
        // Create test data
        RealColorImage image = new RealColorImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);

        // Apply the method
        Image result = clip.apply(image);

        // Assertions
        Assert.assertEquals(image, result);
    }

    @Test
    public void testApply_RealColorImage_ROI() {
        // Create test data
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(0.0f, 1.0f);

        // Apply the method
        Image result = clip.apply(image, roi);

        // Assertions
        Assert.assertEquals(image, result);
    }
}