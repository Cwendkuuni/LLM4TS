import jigl.image.*;
import jigl.image.io.*;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApply_GrayImage_ROI() {
        Clip clip = new Clip(0, 255);
        GrayImage image = new GrayImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApply_RealGrayImage_ROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealGrayImage image = new RealGrayImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApply_ColorImage_ROI() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApply_RealColorImage_ROI() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(50, 50);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }
}