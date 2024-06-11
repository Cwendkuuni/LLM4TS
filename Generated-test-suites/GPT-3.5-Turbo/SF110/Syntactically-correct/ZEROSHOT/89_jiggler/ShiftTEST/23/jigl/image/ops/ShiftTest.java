import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.ops.Shift;

public class ShiftTest {
    
    @Test
    public void testApply_GrayImage() {
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
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 5);
        expected.set(1, 0, 6);
        expected.set(2, 0, 9);
        expected.set(0, 1, 8);
        expected.set(1, 1, 9);
        expected.set(2, 1, 0);
        expected.set(0, 2, 0);
        expected.set(1, 2, 0);
        expected.set(2, 2, 0);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
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
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(3, 3);
        expected.set(0, 0, 5.0f);
        expected.set(1, 0, 6.0f);
        expected.set(2, 0, 9.0f);
        expected.set(0, 1, 8.0f);
        expected.set(1, 1, 9.0f);
        expected.set(2, 1, 0.0f);
        expected.set(0, 2, 0.0f);
        expected.set(1, 2, 0.0f);
        expected.set(2, 2, 0.0f);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(3, 3);
        GrayImage plane0 = new GrayImage(3, 3);
        plane0.set(0, 0, 1);
        plane0.set(1, 0, 2);
        plane0.set(2, 0, 3);
        plane0.set(0, 1, 4);
        plane0.set(1, 1, 5);
        plane0.set(2, 1, 6);
        plane0.set(0, 2, 7);
        plane0.set(1, 2, 8);
        plane0.set(2, 2, 9);
        
        GrayImage plane1 = new GrayImage(3, 3);
        plane1.set(0, 0, 1);
        plane1.set(1, 0, 2);
        plane1.set(2, 0, 3);
        plane1.set(0, 1, 4);
        plane1.set(1, 1, 5);
        plane1.set(2, 1, 6);
        plane1.set(0, 2, 7);
        plane1.set(1, 2, 8);
        plane1.set(2, 2, 9);
        
        GrayImage plane2 = new GrayImage(3, 3);
        plane2.set(0, 0, 1);
        plane2.set(1, 0, 2);
        plane2.set(2, 0, 3);
        plane2.set(0, 1, 4);
        plane2.set(1, 1, 5);
        plane2.set(2, 1, 6);
        plane2.set(0, 2, 7);
        plane2.set(1, 2, 8);
        plane2.set(2, 2, 9);
        
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);
        
        ColorImage expected = new ColorImage(3, 3);
        GrayImage expectedPlane0 = new GrayImage(3, 3);
        expectedPlane0.set(0, 0, 5);
        expectedPlane0.set(1, 0, 6);
        expectedPlane0.set(2, 0, 9);
        expectedPlane0.set(0, 1, 8);
        expectedPlane0.set(1, 1, 9);
        expectedPlane0.set(2, 1, 0);
        expectedPlane0.set(0, 2, 0);
        expectedPlane0.set(1, 2, 0);
        expectedPlane0.set(2, 2, 0);
        
        GrayImage expectedPlane1 = new GrayImage(3, 3);
        expectedPlane1.set(0, 0, 5);
        expectedPlane1.set(1, 0, 6);
        expectedPlane1.set(2, 0, 9);
        expectedPlane1.set(0, 1, 8);
        expectedPlane1.set(1, 1, 9);
        expectedPlane1.set(2, 1, 0);
        expectedPlane1.set(0, 2, 0);
        expectedPlane1.set(1, 2, 0);
        expectedPlane1.set(2, 2, 0);
        
        GrayImage expectedPlane2 = new GrayImage(3, 3);
        expectedPlane2.set(0, 0, 5);
        expectedPlane2.set(1, 0, 6);
        expectedPlane2.set(2, 0, 9);
        expectedPlane2.set(0, 1, 8);
        expectedPlane2.set(1, 1, 9);
        expectedPlane2.set(2, 1, 0);
        expectedPlane2.set(0, 2, 0);
        expectedPlane2.set(1, 2, 0);
        expectedPlane2.set(2, 2, 0);
        
        expected.setPlane(0, expectedPlane0);
        expected.setPlane(1, expectedPlane1);
        expected.setPlane(2, expectedPlane2);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(3, 3);
        RealGrayImage plane0 = new RealGrayImage(3, 3);
        plane0.set(0, 0, 1.0f);
        plane0.set(1, 0, 2.0f);
        plane0.set(2, 0, 3.0f);
        plane0.set(0, 1, 4.0f);
        plane0.set(1, 1, 5.0f);
        plane0.set(2, 1, 6.0f);
        plane0.set(0, 2, 7.0f);
        plane0.set(1, 2, 8.0f);
        plane0.set(2, 2, 9.0f);
        
        RealGrayImage plane1 = new RealGrayImage(3, 3);
        plane1.set(0, 0, 1.0f);
        plane1.set(1, 0, 2.0f);
        plane1.set(2, 0, 3.0f);
        plane1.set(0, 1, 4.0f);
        plane1.set(1, 1, 5.0f);
        plane1.set(2, 1, 6.0f);
        plane1.set(0, 2, 7.0f);
        plane1.set(1, 2, 8.0f);
        plane1.set(2, 2, 9.0f);
        
        RealGrayImage plane2 = new RealGrayImage(3, 3);
        plane2.set(0, 0, 1.0f);
        plane2.set(1, 0, 2.0f);
        plane2.set(2, 0, 3.0f);
        plane2.set(0, 1, 4.0f);
        plane2.set(1, 1, 5.0f);
        plane2.set(2, 1, 6.0f);
        plane2.set(0, 2, 7.0f);
        plane2.set(1, 2, 8.0f);
        plane2.set(2, 2, 9.0f);
        
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);
        
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);
        
        RealColorImage expected = new RealColorImage(3, 3);
        RealGrayImage expectedPlane0 = new RealGrayImage(3, 3);
        expectedPlane0.set(0, 0, 5.0f);
        expectedPlane0.set(1, 0, 6.0f);
        expectedPlane0.set(2, 0, 9.0f);
        expectedPlane0.set(0, 1, 8.0f);
        expectedPlane0.set(1, 1, 9.0f);
        expectedPlane0.set(2, 1, 0.0f);
        expectedPlane0.set(0, 2, 0.0f);
        expectedPlane0.set(1, 2, 0.0f);
        expectedPlane0.set(2, 2, 0.0f);
        
        RealGrayImage expectedPlane1 = new RealGrayImage(3, 3);
        expectedPlane1.set(0, 0, 5.0f);
        expectedPlane1.set(1, 0, 6.0f);
        expectedPlane1.set(2, 0, 9.0f);
        expectedPlane1.set(0, 1, 8.0f);
        expectedPlane1.set(1, 1, 9.0f);
        expectedPlane1.set(2, 1, 0.0f);
        expectedPlane1.set(0, 2, 0.0f);
        expectedPlane1.set(1, 2, 0.0f);
        expectedPlane1.set(2, 2, 0.0f);
        
        RealGrayImage expectedPlane2 = new RealGrayImage(3, 3);
        expectedPlane2.set(0, 0, 5.0f);
        expectedPlane2.set(1, 0, 6.0f);
        expectedPlane2.set(2, 0, 9.0f);
        expectedPlane2.set(0, 1, 8.0f);
        expectedPlane2.set(1, 1, 9.0f);
        expectedPlane2.set(2, 1, 0.0f);
        expectedPlane2.set(0, 2, 0.0f);
        expectedPlane2.set(1, 2, 0.0f);
        expectedPlane2.set(2, 2, 0.0f);
        
        expected.setPlane(0, expectedPlane0);
        expected.setPlane(1, expectedPlane1);
        expected.setPlane(2, expectedPlane2);
        
        assertEquals(expected, result);
    }
}