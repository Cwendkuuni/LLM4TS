package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import jigl.image.ops.Clip;
import jigl.image.*;

public class ClipTest {

    @Test
    public void testConstructor() {
        // testing int constructor
        Clip clip1 = new Clip(0, 255);
        assertEquals(0, clip1.int_min);
        assertEquals(255, clip1.int_max);
        
        // testing float constructor
        Clip clip2 = new Clip(0.0f, 1.0f);
        assertEquals(0.0f, clip2.float_min, 0.001);
        assertEquals(1.0f, clip2.float_max, 0.001);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 50);
        image.set(0, 1, 100);
        image.set(0, 2, 150);
        image.set(1, 0, 200);
        image.set(1, 1, 100);
        image.set(1, 2, 75);
        image.set(2, 0, 175);
        image.set(2, 1, 125);
        image.set(2, 2, 30);
        
        Clip clip = new Clip(50, 150);
        GrayImage result = (GrayImage) clip.apply(image);
        
        assertEquals(50, result.get(0, 0));
        assertEquals(100, result.get(0, 1));
        assertEquals(150, result.get(0, 2));
        assertEquals(150, result.get(1, 0));
        assertEquals(100, result.get(1, 1));
        assertEquals(75, result.get(1, 2));
        assertEquals(150, result.get(2, 0));
        assertEquals(125, result.get(2, 1));
        assertEquals(50, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.1f);
        image.set(0, 1, 0.5f);
        image.set(0, 2, 0.8f);
        image.set(1, 0, 0.9f);
        image.set(1, 1, 0.55f);
        image.set(1, 2, 1.2f);
        image.set(2, 0, 1.5f);
        image.set(2, 1, 1.0f);
        image.set(2, 2, 0.0f);
        
        Clip clip = new Clip(0.5f, 1.0f);
        RealGrayImage result = (RealGrayImage) clip.apply(image);
        
        assertEquals(0.1f, result.get(0, 0), 0.001);
        assertEquals(0.5f, result.get(0, 1), 0.001);
        assertEquals(0.8f, result.get(0, 2), 0.001);
        assertEquals(0.9f, result.get(1, 0), 0.001);
        assertEquals(0.55f, result.get(1, 1), 0.001);
        assertEquals(1.0f, result.get(1, 2), 0.001);
        assertEquals(1.0f, result.get(2, 0), 0.001);
        assertEquals(1.0f, result.get(2, 1), 0.001);
        assertEquals(0.5f, result.get(2, 2), 0.001);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new RGB(50, 50, 50));
        image.set(0, 1, new RGB(100, 100, 100));
        image.set(0, 2, new RGB(150, 150, 150));
        image.set(1, 0, new RGB(200, 200, 200));
        image.set(1, 1, new RGB(100, 100, 100));
        image.set(1, 2, new RGB(75, 75, 75));
        image.set(2, 0, new RGB(175, 175, 175));
        image.set(2, 1, new RGB(125, 125, 125));
        image.set(2, 2, new RGB(30, 30, 30));
        
        Clip clip = new Clip(50, 150);
        ColorImage result = (ColorImage) clip.apply(image);
        
        assertEquals(new RGB(50, 50, 50), result.get(0, 0));
        assertEquals(new RGB(100, 100, 100), result.get(0, 1));
        assertEquals(new RGB(150, 150, 150), result.get(0, 2));
        assertEquals(new RGB(150, 150, 150), result.get(1, 0));
        assertEquals(new RGB(100, 100, 100), result.get(1, 1));
        assertEquals(new RGB(75, 75, 75), result.get(1, 2));
        assertEquals(new RGB(150, 150, 150), result.get(2, 0));
        assertEquals(new RGB(125, 125, 125), result.get(2, 1));
        assertEquals(new RGB(50, 50, 50), result.get(2, 2));
    }

}