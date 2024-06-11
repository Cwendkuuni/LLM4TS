package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.ROI;

public class ClipTest {

    @Test
    public void testConstructorInt() {
        Clip clip = new Clip(10, 20);
        assertEquals(10, clip.int_min);
        assertEquals(20, clip.int_max);
        assertEquals(0.0f, clip.float_min, 0.0f);
        assertEquals(0.0f, clip.float_max, 0.0f);
    }

    @Test
    public void testConstructorFloat() {
        Clip clip = new Clip(0.5f, 1.5f);
        assertEquals(0, clip.int_min);
        assertEquals(0, clip.int_max);
        assertEquals(0.5f, clip.float_min, 0.0f);
        assertEquals(1.5f, clip.float_max, 0.0f);
    }

    @Test
    public void testApplyGrayImage() {
        Clip clip = new Clip(10, 20);
        GrayImage image = new GrayImage(10, 10);
        GrayImage result = new GrayImage(10, 10);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                image.set(x, y, 15);
                result.set(x, y, 15);
            }
        }
        
        ROI roi = new ROI(0, 0, 9, 9);
        clip.apply(image, roi);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                assertEquals(result.get(x, y), image.get(x, y));
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        Clip clip = new Clip(0.5f, 1.5f);
        RealGrayImage image = new RealGrayImage(10, 10);
        RealGrayImage result = new RealGrayImage(10, 10);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                image.set(x, y, 1.0f);
                result.set(x, y, 1.0f);
            }
        }
        
        ROI roi = new ROI(0, 0, 9, 9);
        clip.apply(image, roi);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                assertEquals(result.get(x, y), image.get(x, y), 0.0f);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        Clip clip = new Clip(0, 255);
        ColorImage image = new ColorImage(10, 10);
        ColorImage result = new ColorImage(10, 10);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                image.set(x, y, 100, 100, 100);
                result.set(x, y, 100, 100, 100);
            }
        }
        
        ROI roi = new ROI(0, 0, 9, 9);
        clip.apply(image, roi);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                assertEquals(result.get(x, y, ColorImage.RED), image.get(x, y, ColorImage.RED));
                assertEquals(result.get(x, y, ColorImage.GREEN), image.get(x, y, ColorImage.GREEN));
                assertEquals(result.get(x, y, ColorImage.BLUE), image.get(x, y, ColorImage.BLUE));
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        Clip clip = new Clip(0.0f, 1.0f);
        RealColorImage image = new RealColorImage(10, 10);
        RealColorImage result = new RealColorImage(10, 10);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                image.set(x, y, 0.5f, 0.5f, 0.5f);
                result.set(x, y, 0.5f, 0.5f, 0.5f);
            }
        }
        
        ROI roi = new ROI(0, 0, 9, 9);
        clip.apply(image, roi);
        
        for(int y=0; y<10; y++) {
            for(int x=0; x<10; x++) {
                assertEquals(result.get(x, y, RealColorImage.RED), image.get(x, y, RealColorImage.RED), 0.0f);
                assertEquals(result.get(x, y, RealColorImage.GREEN), image.get(x, y, RealColorImage.GREEN), 0.0f);
                assertEquals(result.get(x, y, RealColorImage.BLUE), image.get(x, y, RealColorImage.BLUE), 0.0f);
            }
        }
    }

    @Test
    public void testMain() {
        // TODO: Implement test case
    }
}