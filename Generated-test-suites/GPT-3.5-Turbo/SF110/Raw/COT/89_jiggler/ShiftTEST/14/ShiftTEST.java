import jigl.image.*;
import jigl.image.exceptions.*;
import jigl.image.ops.Shift;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testConstructor() {
        Shift shift = new Shift(1, 2, Shift.WRAP);
        assertEquals(1, shift.xoffset);
        assertEquals(2, shift.yoffset);
        assertTrue(shift.wrap);
        
        Shift shift2 = new Shift(3, 4, Shift.NOWRAP);
        assertEquals(3, shift2.xoffset);
        assertEquals(4, shift2.yoffset);
        assertFalse(shift2.wrap);
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
        assertEquals(9, result.get(2, 2));
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

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApplyRealGrayImageWrap() {
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

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(image);

        assertEquals(5.0f, result.get(0, 0), 0.01f);
        assertEquals(6.0f, result.get(1, 0), 0.01f);
        assertEquals(3.0f, result.get(2, 0), 0.01f);
        assertEquals(8.0f, result.get(0, 1), 0.01f);
        assertEquals(9.0f, result.get(1, 1), 0.01f);
        assertEquals(6.0f, result.get(2, 1), 0.01f);
        assertEquals(2.0f, result.get(0, 2), 0.01f);
        assertEquals(3.0f, result.get(1, 2), 0.01f);
        assertEquals(9.0f, result.get(2, 2), 0.01f);
    }
    
    @Test
    public void testApplyRealGrayImageNoWrap() {
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

        assertEquals(0.0f, result.get(0, 0), 0.01f);
        assertEquals(0.0f, result.get(1, 0), 0.01f);
        assertEquals(0.0f, result.get(2, 0), 0.01f);
        assertEquals(0.0f, result.get(0, 1), 0.01f);
        assertEquals(1.0f, result.get(1, 1), 0.01f);
        assertEquals(0.0f, result.get(2, 1), 0.01f);
        assertEquals(0.0f, result.get(0, 2), 0.01f);
        assertEquals(0.0f, result.get(1, 2), 0.01f);
        assertEquals(0.0f, result.get(2, 2), 0.01f);
    }

    @Test
    public void testApplyColorImageWrap() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));

        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RealGrayImage plane = result.plane(i);
                assertEquals(0.0f, plane.get(0, 0), 0.01f);
                assertEquals(0.0f, plane.get(1, 0), 0.01f);
                assertEquals(0.0f, plane.get(0, 1), 0.01f);
                assertEquals(0.0f, plane.get(1, 1), 0.01f);
            }
        }
    }
    
    @Test
    public void testApplyColorImageNoWrap() {
        ColorImage image = new ColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RealGrayImage plane = result.plane(i);
                assertEquals(0.0f, plane.get(0, 0), 0.01f);
                assertEquals(0.0f, plane.get(1, 0), 0.01f);
                assertEquals(0.0f, plane.get(0, 1), 0.01f);
                assertEquals(0.0f, plane.get(1, 1), 0.01f);
            }
        }
    }

    @Test
    public void testApplyRealColorImageWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));

        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RealGrayImage plane = result.plane(i);
                assertEquals(0.0f, plane.get(0, 0), 0.01f);
                assertEquals(0.0f, plane.get(1, 0), 0.01f);
                assertEquals(0.0f, plane.get(0, 1), 0.01f);
                assertEquals(0.0f, plane.get(1, 1), 0.01f);
            }
        }
    }
    
    @Test
    public void testApplyRealColorImageNoWrap() {
        RealColorImage image = new RealColorImage(3, 3);
        image.setPlane(0, new RealGrayImage(3, 3));
        image.setPlane(1, new RealGrayImage(3, 3));
        image.setPlane(2, new RealGrayImage(3, 3));

        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(image);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                RealGrayImage plane = result.plane(i);
                assertEquals(0.0f, plane.get(0, 0), 0.01f);
                assertEquals(0.0f, plane.get(1, 0), 0.01f);
                assertEquals(0.0f, plane.get(0, 1), 0.01f);
                assertEquals(0.0f, plane.get(1, 1), 0.01f);
            }
        }
    }
}