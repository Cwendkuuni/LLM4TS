
package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.ColorImage;
import jigl.image.GrayImage;
import jigl.image.RealColorImage;
import jigl.image.RealGrayImage;
import jigl.image.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.Image;

public class ClipTest {

    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(5, 5);
        image.fill(127);
        
        GrayImage expectedImage = new GrayImage(5, 5);
        expectedImage.fill(0);
        
        Image result = clip.apply(image);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_GrayImageROI() {
        Clip clip = new Clip(0, 0);
        GrayImage image = new GrayImage(5, 5);
        image.fill(200);
        ROI roi = new ROI(1, 1, 3, 3);
        
        GrayImage expectedImage = new GrayImage(5, 5);
        expectedImage.fill(200);
        expectedImage.set(1, 1, 0);
        expectedImage.set(1, 3, 0);
        expectedImage.set(3, 1, 0);
        expectedImage.set(3, 3, 0);
        
        Image result = clip.apply(image, roi);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(0.5f);
        
        RealGrayImage expectedImage = new RealGrayImage(5, 5);
        expectedImage.fill(0.0f);
        
        Image result = clip.apply(image);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_RealGrayImageROI() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealGrayImage image = new RealGrayImage(5, 5);
        image.fill(0.8f);
        ROI roi = new ROI(1, 1, 3, 3);
        
        RealGrayImage expectedImage = new RealGrayImage(5, 5);
        expectedImage.fill(0.8f);
        expectedImage.set(1, 1, 0.0f);
        expectedImage.set(1, 3, 0.0f);
        expectedImage.set(3, 1, 0.0f);
        expectedImage.set(3, 3, 0.0f);
        
        Image result = clip.apply(image, roi);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(0, 0);
        ColorImage image = new ColorImage(5, 5);
        image.fill(127);
        
        ColorImage expectedImage = new ColorImage(5, 5);
        expectedImage.fill(0);
        
        Image result = clip.apply(image);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_ColorImageROI() {
        Clip clip = new Clip(0, 0);
        ColorImage image = new ColorImage(5, 5);
        image.fill(200);
        ROI roi = new ROI(1, 1, 3, 3);
        
        ColorImage expectedImage = new ColorImage(5, 5);
        expectedImage.fill(200);
        expectedImage.set(1, 1, 0);
        expectedImage.set(1, 3, 0);
        expectedImage.set(3, 1, 0);
        expectedImage.set(3, 3, 0);
        
        Image result = clip.apply(image, roi);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(0.5f);
        
        RealColorImage expectedImage = new RealColorImage(5, 5);
        expectedImage.fill(0.0f);
        
        Image result = clip.apply(image);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }

    @Test
    public void testApply_RealColorImageROI() {
        Clip clip = new Clip(0.0f, 0.0f);
        RealColorImage image = new RealColorImage(5, 5);
        image.fill(0.8f);
        ROI roi = new ROI(1, 1, 3, 3);
        
        RealColorImage expectedImage = new RealColorImage(5, 5);
        expectedImage.fill(0.8f);
        expectedImage.set(1, 1, 0.0f);
        expectedImage.set(1, 3, 0.0f);
        expectedImage.set(3, 1, 0.0f);
        expectedImage.set(3, 3, 0.0f);
        
        Image result = clip.apply(image, roi);
        
        assertArrayEquals(expectedImage.getData(), result.getData());
    }
}
