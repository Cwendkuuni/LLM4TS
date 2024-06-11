import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage_NoWrap() {
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
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0));
        assertEquals(6, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(9, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_GrayImage_Wrap() {
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
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0));
        assertEquals(6, result.get(1, 0));
        assertEquals(3, result.get(2, 0));
        assertEquals(8, result.get(0, 1));
        assertEquals(9, result.get(1, 1));
        assertEquals(6, result.get(2, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(3, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage_NoWrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.2);
        image.set(1, 0, 2.4);
        image.set(2, 0, 3.6);
        image.set(0, 1, 4.8);
        image.set(1, 1, 5.0);
        image.set(2, 1, 6.2);
        image.set(0, 2, 7.4);
        image.set(1, 2, 8.6);
        image.set(2, 2, 9.8);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(5.0, result.get(0, 0));
        assertEquals(6.2, result.get(1, 0));
        assertEquals(0.0, result.get(2, 0), 0.001);
        assertEquals(8.6, result.get(0, 1));
        assertEquals(9.8, result.get(1, 1));
        assertEquals(0.0, result.get(2, 1), 0.001);
        assertEquals(0.0, result.get(0, 2), 0.001);
        assertEquals(0.0, result.get(1, 2), 0.001);
        assertEquals(0.0, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApply_RealGrayImage_Wrap() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 1.2);
        image.set(1, 0, 2.4);
        image.set(2, 0, 3.6);
        image.set(0, 1, 4.8);
        image.set(1, 1, 5.0);
        image.set(2, 1, 6.2);
        image.set(0, 2, 7.4);
        image.set(1, 2, 8.6);
        image.set(2, 2, 9.8);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(5.0, result.get(0, 0));
        assertEquals(6.2, result.get(1, 0));
        assertEquals(3.6, result.get(2, 0), 0.001);
        assertEquals(8.6, result.get(0, 1));
        assertEquals(9.8, result.get(1, 1));
        assertEquals(6.2, result.get(2, 1), 0.001);
        assertEquals(2.4, result.get(0, 2), 0.001);
        assertEquals(3.6, result.get(1, 2), 0.001);
        assertEquals(0.0, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApply_ColorImage_NoWrap() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1, 1, 1);
        image.set(1, 0, 2, 2, 2);
        image.set(2, 0, 3, 3, 3);
        image.set(0, 1, 4, 4, 4);
        image.set(1, 1, 5, 5, 5);
        image.set(2, 1, 6, 6, 6);
        image.set(0, 2, 7, 7, 7);
        image.set(1, 2, 8, 8, 8);
        image.set(2, 2, 9, 9, 9);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0).getRed());
        assertEquals(6, result.get(1, 0).getRed());
        assertEquals(0, result.get(2, 0).getRed());
        assertEquals(8, result.get(0, 1).getRed());
        assertEquals(9, result.get(1, 1).getRed());
        assertEquals(0, result.get(2, 1).getRed());
        assertEquals(0, result.get(0, 2).getRed());
        assertEquals(0, result.get(1, 2).getRed());
        assertEquals(0, result.get(2, 2).getRed());
        
        assertEquals(5, result.get(0, 0).getGreen());
        assertEquals(6, result.get(1, 0).getGreen());
        assertEquals(0, result.get(2, 0).getGreen());
        assertEquals(8, result.get(0, 1).getGreen());
        assertEquals(9, result.get(1, 1).getGreen());
        assertEquals(0, result.get(2, 1).getGreen());
        assertEquals(0, result.get(0, 2).getGreen());
        assertEquals(0, result.get(1, 2).getGreen());
        assertEquals(0, result.get(2, 2).getGreen());
        
        assertEquals(5, result.get(0, 0).getBlue());
        assertEquals(6, result.get(1, 0).getBlue());
        assertEquals(0, result.get(2, 0).getBlue());
        assertEquals(8, result.get(0, 1).getBlue());
        assertEquals(9, result.get(1, 1).getBlue());
        assertEquals(0, result.get(2, 1).getBlue());
        assertEquals(0, result.get(0, 2).getBlue());
        assertEquals(0, result.get(1, 2).getBlue());
        assertEquals(0, result.get(2, 2).getBlue());
    }
    
    @Test
    public void testApply_ColorImage_Wrap() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, 1, 1, 1);
        image.set(1, 0, 2, 2, 2);
        image.set(2, 0, 3, 3, 3);
        image.set(0, 1, 4, 4, 4);
        image.set(1, 1, 5, 5, 5);
        image.set(2, 1, 6, 6, 6);
        image.set(0, 2, 7, 7, 7);
        image.set(1, 2, 8, 8, 8);
        image.set(2, 2, 9, 9, 9);
        
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);
        
        assertEquals(5, result.get(0, 0).getRed());
        assertEquals(6, result.get(1, 0).getRed());
        assertEquals(3, result.get(2, 0).getRed());
        assertEquals(8, result.get(0, 1).getRed());
        assertEquals(9, result.get(1, 1).getRed());
        assertEquals(6, result.get(2, 1).getRed());
        assertEquals(2, result.get(0, 2).getRed());
        assertEquals(3, result.get(1, 2).getRed());
        assertEquals(0, result.get(2, 2).getRed());
        
        assertEquals(5, result.get(0, 0).getGreen());
        assertEquals(6, result.get(1, 0).getGreen());
        assertEquals(3, result.get(2, 0).getGreen());
        assertEquals(8, result.get(0, 1).getGreen());
        assertEquals(9, result.get(1, 1).getGreen());
        assertEquals(6, result.get(2, 1).getGreen());
        assertEquals(2, result.get(0, 2).getGreen());
        assertEquals(3, result.get(1, 2).getGreen());
        assertEquals(0, result.get(2, 2).getGreen());
        
        assertEquals(5, result.get(0, 0).getBlue());
        assertEquals(6, result.get(1, 0).getBlue());
        assertEquals(3, result.get(2, 0).getBlue());
        assertEquals(8, result.get(0, 1).getBlue());
        assertEquals(9, result.get(1, 1).getBlue());
        assertEquals(6, result.get(2, 1).getBlue());
        assertEquals(2, result.get(0, 2).getBlue());
        assertEquals(3, result.get(1, 2).getBlue());
        assertEquals(0, result.get(2, 2).getBlue());
    }
    
}