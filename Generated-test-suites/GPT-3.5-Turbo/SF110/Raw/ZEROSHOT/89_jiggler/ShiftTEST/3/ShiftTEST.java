import jigl.image.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(4, 4);
        image.fill(255);
        
        Shift shift = new Shift(1, 1, 0);
        GrayImage result = shift.apply(image);
        
        GrayImage expected = new GrayImage(4, 4);
        expected.fill(0);
        expected.set(1, 1, 255);
        expected.set(2, 2, 255);
        expected.set(3, 3, 255);
        
        assertEquals(expected, result);
        
        shift = new Shift(-1, -1, 0);
        result = shift.apply(image);
        
        expected = new GrayImage(4, 4);
        expected.fill(0);
        expected.set(0, 0, 255);
        expected.set(1, 1, 255);
        expected.set(2, 2, 255);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(4, 4);
        image.fill(1.0f);
        
        Shift shift = new Shift(1, 1, 0);
        RealGrayImage result = shift.apply(image);
        
        RealGrayImage expected = new RealGrayImage(4, 4);
        expected.fill(0.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 1.0f);
        expected.set(3, 3, 1.0f);
        
        assertEquals(expected, result);
        
        shift = new Shift(-1, -1, 0);
        result = shift.apply(image);
        
        expected = new RealGrayImage(4, 4);
        expected.fill(0.0f);
        expected.set(0, 0, 1.0f);
        expected.set(1, 1, 1.0f);
        expected.set(2, 2, 1.0f);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(4, 4);
        image.setPlane(0, new GrayImage(4, 4));
        image.setPlane(1, new GrayImage(4, 4));
        image.setPlane(2, new GrayImage(4, 4));
        
        image.plane(0).fill(255);
        image.plane(1).fill(127);
        image.plane(2).fill(63);
        
        Shift shift = new Shift(1, 1, 0);
        ColorImage result = shift.apply(image);
        
        ColorImage expected = new ColorImage(4, 4);
        expected.setPlane(0, new GrayImage(4, 4));
        expected.setPlane(1, new GrayImage(4, 4));
        expected.setPlane(2, new GrayImage(4, 4));
        
        expected.plane(0).fill(0);
        expected.plane(1).fill(0);
        expected.plane(2).fill(0);
        
        expected.plane(0).set(1, 1, 255);
        expected.plane(1).set(1, 1, 127);
        expected.plane(2).set(1, 1, 63);
        
        expected.plane(0).set(2, 2, 255);
        expected.plane(1).set(2, 2, 127);
        expected.plane(2).set(2, 2, 63);
        
        expected.plane(0).set(3, 3, 255);
        expected.plane(1).set(3, 3, 127);
        expected.plane(2).set(3, 3, 63);
        
        assertEquals(expected, result);
        
        shift = new Shift(-1, -1, 0);
        result = shift.apply(image);
        
        expected = new ColorImage(4, 4);
        expected.setPlane(0, new GrayImage(4, 4));
        expected.setPlane(1, new GrayImage(4, 4));
        expected.setPlane(2, new GrayImage(4, 4));
        
        expected.plane(0).fill(0);
        expected.plane(1).fill(0);
        expected.plane(2).fill(0);
        
        expected.plane(0).set(0, 0, 255);
        expected.plane(1).set(0, 0, 127);
        expected.plane(2).set(0, 0, 63);
        
        expected.plane(0).set(1, 1, 255);
        expected.plane(1).set(1, 1, 127);
        expected.plane(2).set(1, 1, 63);
        
        expected.plane(0).set(2, 2, 255);
        expected.plane(1).set(2, 2, 127);
        expected.plane(2).set(2, 2, 63);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(4, 4);
        image.setPlane(0, new RealGrayImage(4, 4));
        image.setPlane(1, new RealGrayImage(4, 4));
        image.setPlane(2, new RealGrayImage(4, 4));
        
        image.plane(0).fill(1.0f);
        image.plane(1).fill(0.5f);
        image.plane(2).fill(0.25f);
        
        Shift shift = new Shift(1, 1, 0);
        RealColorImage result = shift.apply(image);
        
        RealColorImage expected = new RealColorImage(4, 4);
        expected.setPlane(0, new RealGrayImage(4, 4));
        expected.setPlane(1, new RealGrayImage(4, 4));
        expected.setPlane(2, new RealGrayImage(4, 4));
        
        expected.plane(0).fill(0.0f);
        expected.plane(1).fill(0.0f);
        expected.plane(2).fill(0.0f);
        
        expected.plane(0).set(1, 1, 1.0f);
        expected.plane(1).set(1, 1, 0.5f);
        expected.plane(2).set(1, 1, 0.25f);
        
        expected.plane(0).set(2, 2, 1.0f);
        expected.plane(1).set(2, 2, 0.5f);
        expected.plane(2).set(2, 2, 0.25f);
        
        expected.plane(0).set(3, 3, 1.0f);
        expected.plane(1).set(3, 3, 0.5f);
        expected.plane(2).set(3, 3, 0.25f);
        
        assertEquals(expected, result);
        
        shift = new Shift(-1, -1, 0);
        result = shift.apply(image);
        
        expected = new RealColorImage(4, 4);
        expected.setPlane(0, new RealGrayImage(4, 4));
        expected.setPlane(1, new RealGrayImage(4, 4));
        expected.setPlane(2, new RealGrayImage(4, 4));
        
        expected.plane(0).fill(0.0f);
        expected.plane(1).fill(0.0f);
        expected.plane(2).fill(0.0f);
        
        expected.plane(0).set(0, 0, 1.0f);
        expected.plane(1).set(0, 0, 0.5f);
        expected.plane(2).set(0, 0, 0.25f);
        
        expected.plane(0).set(1, 1, 1.0f);
        expected.plane(1).set(1, 1, 0.5f);
        expected.plane(2).set(1, 1, 0.25f);
        
        expected.plane(0).set(2, 2, 1.0f);
        expected.plane(1).set(2, 2, 0.5f);
        expected.plane(2).set(2, 2, 0.25f);
        
        assertEquals(expected, result);
    }

}