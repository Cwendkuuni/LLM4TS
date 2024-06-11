import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {

    @Test
    public void testGrayImageApply() {
        GrayImage image = new GrayImage(3, 3);
        image.set(1, 1, 255);
        
        Shift shift = new Shift(1, 1, 0);
        GrayImage result = shift.apply(image);
        
        assertEquals(255, result.get(0, 0));
    }

    @Test
    public void testRealGrayImageApply() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(1, 1, 0.5f);
        
        Shift shift = new Shift(1, 1, 1);
        RealGrayImage result = shift.apply(image);
        
        assertEquals(0.5f, result.get(0, 0), 0.001);
    }

    @Test
    public void testColorImageApply() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new GrayImage(3, 3));
        image.setPlane(1, new GrayImage(3, 3));
        image.setPlane(2, new GrayImage(3, 3));
        
        Shift shift = new Shift(-1, 1, 0);
        ColorImage result = shift.apply(image);
        
        assertEquals(0, result.plane(0).get(0, 2));
    }

    @Test
    public void testRealColorImageApply() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));
        
        Shift shift = new Shift(2, -2, 1);
        RealColorImage result = shift.apply(image);
        
        assertEquals(0, result.plane(1).get(0, 0), 0.001);
    }
}