import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage_wrap() {
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
        
        Shift shift = new Shift(1, 1, 1);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 9);
        expected.set(1, 0, 7);
        expected.set(2, 0, 8);
        expected.set(0, 1, 3);
        expected.set(1, 1, 1);
        expected.set(2, 1, 2);
        expected.set(0, 2, 6);
        expected.set(1, 2, 4);
        expected.set(2, 2, 5);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_GrayImage_noWrap() {
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
        
        Shift shift = new Shift(1, 1, 0);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(1, 0, 0);
        expected.set(2, 0, 0);
        expected.set(0, 1, 0);
        expected.set(1, 1, 1);
        expected.set(2, 1, 2);
        expected.set(0, 2, 0);
        expected.set(1, 2, 4);
        expected.set(2, 2, 5);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage_wrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 0, 2.0f);
        image.set(2, 0, 3.0f);
        image.set(0, 1, 4.0f);
        image.set(1, 1, 5.0f);
        image.set(2, 1, 6.0f);
        image.set(0, 2, 7.0f);
        image.set(1, 2, 8.0f);
        image.set(2, 2, 9.0f);
        
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 9.0f);
        expected.set(1, 0, 7.0f);
        expected.set(2, 0, 8.0f);
        expected.set(0, 1, 3.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 1, 2.0f);
        expected.set(0, 2, 6.0f);
        expected.set(1, 2, 4.0f);
        expected.set(2, 2, 5.0f);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage_noWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.0f);
        image.set(1, 0, 2.0f);
        image.set(2, 0, 3.0f);
        image.set(0, 1, 4.0f);
        image.set(1, 1, 5.0f);
        image.set(2, 1, 6.0f);
        image.set(0, 2, 7.0f);
        image.set(1, 2, 8.0f);
        image.set(2, 2, 9.0f);
        
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 0.0f);
        expected.set(1, 0, 0.0f);
        expected.set(2, 0, 0.0f);
        expected.set(0, 1, 0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 1, 2.0f);
        expected.set(0, 2, 0.0f);
        expected.set(1, 2, 4.0f);
        expected.set(2, 2, 5.0f);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage_wrap() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new RGB(1, 2, 3));
        image.set(1, 0, new RGB(4, 5, 6));
        image.set(2, 0, new RGB(7, 8, 9));
        image.set(0, 1, new RGB(10, 11, 12));
        image.set(1, 1, new RGB(13, 14, 15));
        image.set(2, 1, new RGB(16, 17, 18));
        image.set(0, 2, new RGB(19, 20, 21));
        image.set(1, 2, new RGB(22, 23, 24));
        image.set(2, 2, new RGB(25, 26, 27));
        
        Shift shift = new Shift(1, 1, 1);
        ColorImage result = shift.apply(image);
        
        ColorImage expected = new ColorImage(3, 3);
        expected.set(0, 0, new RGB(25, 26, 27));
        expected.set(1, 0, new RGB(22, 23, 24));
        expected.set(2, 0, new RGB(19, 20, 21));
        expected.set(0, 1, new RGB(7, 8, 9));
        expected.set(1, 1, new RGB(1, 2, 3));
        expected.set(2, 1, new RGB(4, 5, 6));
        expected.set(0, 2, new RGB(16, 17, 18));
        expected.set(1, 2, new RGB(13, 14, 15));
        expected.set(2, 2, new RGB(10, 11, 12));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage_wrap() {
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, new RGB(1.0f, 2.0f, 3.0f));
        image.set(1, 0, new RGB(4.0f, 5.0f, 6.0f));
        image.set(2, 0, new RGB(7.0f, 8.0f, 9.0f));
        image.set(0, 1, new RGB(10.0f, 11.0f, 12.0f));
        image.set(1, 1, new RGB(13.0f, 14.0f, 15.0f));
        image.set(2, 1, new RGB(16.0f, 17.0f, 18.0f));
        image.set(0, 2, new RGB(19.0f, 20.0f, 21.0f));
        image.set(1, 2, new RGB(22.0f, 23.0f, 24.0f));
        image.set(2, 2, new RGB(25.0f, 26.0f, 27.0f));
        
        Shift shift = new Shift(1, 1, 1);
        RealColorImage result = shift.apply(image);
        
        RealColorImage expected = new RealColorImage(3, 3);
        expected.set(0, 0, new RGB(25.0f, 26.0f, 27.0f));
        expected.set(1, 0, new RGB(22.0f, 23.0f, 24.0f));
        expected.set(2, 0, new RGB(19.0f, 20.0f, 21.0f));
        expected.set(0, 1, new RGB(7.0f, 8.0f, 9.0f));
        expected.set(1, 1, new RGB(1.0f, 2.0f, 3.0f));
        expected.set(2, 1, new RGB(4.0f, 5.0f, 6.0f));
        expected.set(0, 2, new RGB(16.0f, 17.0f, 18.0f));
        expected.set(1, 2, new RGB(13.0f, 14.0f, 15.0f));
        expected.set(2, 2, new RGB(10.0f, 11.0f, 12.0f));
        
        assertEquals(expected, result);
    }
}