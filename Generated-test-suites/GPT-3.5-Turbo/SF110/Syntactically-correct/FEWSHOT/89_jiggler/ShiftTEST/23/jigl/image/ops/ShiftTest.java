package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

public class ShiftTest {
    @Test
    public void testShiftConstructor() {
        Shift shift = new Shift(3, 4, 1);
        assertEquals(3, shift.xoffset);
        assertEquals(4, shift.yoffset);
        assertEquals(true, shift.wrap);
        Shift shift2 = new Shift(0, 0, 0);
        assertEquals(0, shift2.xoffset);
        assertEquals(0, shift2.yoffset);
        assertEquals(false, shift2.wrap);
    }
    
    @Test
    public void testApplyGrayImageWrap() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(2, 0, 3);
        image.set(0, 1, 4);
        image.set(1, 1, 5);
        image.set(2, 1, 6);
        image.set(0, 2, 7);
        image.set(1, 2, 8);
        image.set(2, 2, 9);
        
        Shift shift = new Shift(1, 2, 1);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(1, 2, 1);
        expected.set(2, 2, 2);
        expected.set(0, 0, 3);
        expected.set(1, 0, 4);
        expected.set(2, 0, 5);
        expected.set(0, 1, 6);
        expected.set(1, 1, 7);
        expected.set(2, 1, 8);
        expected.set(0, 2, 9);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyGrayImageNoWrap() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(2, 0, 3);
        image.set(0, 1, 4);
        image.set(1, 1, 5);
        image.set(2, 1, 6);
        image.set(0, 2, 7);
        image.set(1, 2, 8);
        image.set(2, 2, 9);
        
        Shift shift = new Shift(-1, -2, 0);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 9);
        expected.set(1, 0, 0);
        expected.set(2, 0, 0);
        expected.set(0, 1, 0);
        expected.set(1, 1, 0);
        expected.set(2, 1, 0);
        expected.set(0, 2, 0);
        expected.set(1, 2, 1);
        expected.set(2, 2, 2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyRealGrayImageWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0);
        image.set(1, 0, 2.0);
        image.set(2, 0, 3.0);
        image.set(0, 1, 4.0);
        image.set(1, 1, 5.0);
        image.set(2, 1, 6.0);
        image.set(0, 2, 7.0);
        image.set(1, 2, 8.0);
        image.set(2, 2, 9.0);
        
        Shift shift = new Shift(1, 2, 1);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(1, 2, 1.0);
        expected.set(2, 2, 2.0);
        expected.set(0, 0, 3.0);
        expected.set(1, 0, 4.0);
        expected.set(2, 0, 5.0);
        expected.set(0, 1, 6.0);
        expected.set(1, 1, 7.0);
        expected.set(2, 1, 8.0);
        expected.set(0, 2, 9.0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyRealGrayImageNoWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0);
        image.set(1, 0, 2.0);
        image.set(2, 0, 3.0);
        image.set(0, 1, 4.0);
        image.set(1, 1, 5.0);
        image.set(2, 1, 6.0);
        image.set(0, 2, 7.0);
        image.set(1, 2, 8.0);
        image.set(2, 2, 9.0);
        
        Shift shift = new Shift(-1, -2, 0);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 9.0);
        expected.set(1, 0, 0.0);
        expected.set(2, 0, 0.0);
        expected.set(0, 1, 0.0);
        expected.set(1, 1, 0.0);
        expected.set(2, 1, 0.0);
        expected.set(0, 2, 0.0);
        expected.set(1, 2, 1.0);
        expected.set(2, 2, 2.0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        
        Shift shift = new Shift(1, 2, 1);
        ColorImage result = shift.apply(image);
        
        ColorImage expected = new ColorImage(3, 3);
        expected.setPlane(0, new GrayImage(3, 3));
        expected.setPlane(1, new GrayImage(3, 3));
        expected.setPlane(2, new GrayImage(3, 3));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(1, 2, 1);
        RealColorImage result = shift.apply(image);
        
        RealColorImage expected = new RealColorImage(3, 3);
        expected.setPlane(0, new RealGrayImage(3, 3));
        expected.setPlane(1, new RealGrayImage(3, 3));
        expected.setPlane(2, new RealGrayImage(3, 3));
        
        assertEquals(expected, result);
    }
}