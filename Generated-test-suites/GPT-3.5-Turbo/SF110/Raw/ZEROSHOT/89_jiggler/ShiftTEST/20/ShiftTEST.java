import static org.junit.Assert.*;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.exceptions.*;
import jigl.image.ops.Shift;
import org.junit.Before;
import org.junit.Test;

public class ShiftTest {
    
    private Shift shift;
    
    @Before
    public void setUp() {
        shift = new Shift(2, 3, 1);
    }
    
    //Test apply method for GrayImage
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(4, 4);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(2, 0, 3);
        image.set(3, 0, 4);
        image.set(0, 1, 5);
        image.set(1, 1, 6);
        image.set(2, 1, 7);
        image.set(3, 1, 8);
        image.set(0, 2, 9);
        image.set(1, 2, 10);
        image.set(2, 2, 11);
        image.set(3, 2, 12);
        image.set(0, 3, 13);
        image.set(1, 3, 14);
        image.set(2, 3, 15);
        image.set(3, 3, 16);
        GrayImage expected = new GrayImage(4, 4);
        expected.set(2, 0, 1);
        expected.set(3, 0, 2);
        expected.set(0, 1, 3);
        expected.set(1, 1, 4);
        expected.set(2, 1, 5);
        expected.set(3, 1, 6);
        expected.set(0, 2, 7);
        expected.set(1, 2, 8);
        expected.set(2, 2, 9);
        expected.set(3, 2, 10);
        expected.set(0, 3, 11);
        expected.set(1, 3, 12);
        expected.set(2, 3, 13);
        expected.set(3, 3, 14);
        GrayImage result = shift.apply(image);
        assertEquals(expected, result);
    }
    
    //Test apply method for RealGrayImage
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(4, 4);
        image.set(0, 0, 1.0f);
        image.set(1, 0, 2.0f);
        image.set(2, 0, 3.0f);
        image.set(3, 0, 4.0f);
        image.set(0, 1, 5.0f);
        image.set(1, 1, 6.0f);
        image.set(2, 1, 7.0f);
        image.set(3, 1, 8.0f);
        image.set(0, 2, 9.0f);
        image.set(1, 2, 10.0f);
        image.set(2, 2, 11.0f);
        image.set(3, 2, 12.0f);
        image.set(0, 3, 13.0f);
        image.set(1, 3, 14.0f);
        image.set(2, 3, 15.0f);
        image.set(3, 3, 16.0f);
        RealGrayImage expected = new RealGrayImage(4, 4);
        expected.set(2, 0, 1.0f);
        expected.set(3, 0, 2.0f);
        expected.set(0, 1, 3.0f);
        expected.set(1, 1, 4.0f);
        expected.set(2, 1, 5.0f);
        expected.set(3, 1, 6.0f);
        expected.set(0, 2, 7.0f);
        expected.set(1, 2, 8.0f);
        expected.set(2, 2, 9.0f);
        expected.set(3, 2, 10.0f);
        expected.set(0, 3, 11.0f);
        expected.set(1, 3, 12.0f);
        expected.set(2, 3, 13.0f);
        expected.set(3, 3, 14.0f);
        RealGrayImage result = shift.apply(image);
        assertEquals(expected, result);
    }
    
    //Test apply method for ColorImage
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(4, 4);
        image.set(0, 0, 1, 1, 1);
        image.set(1, 0, 2, 2, 2);
        image.set(2, 0, 3, 3, 3);
        image.set(3, 0, 4, 4, 4);
        image.set(0, 1, 5, 5, 5);
        image.set(1, 1, 6, 6, 6);
        image.set(2, 1, 7, 7, 7);
        image.set(3, 1, 8, 8, 8);
        image.set(0, 2, 9, 9, 9);
        image.set(1, 2, 10, 10, 10);
        image.set(2, 2, 11, 11, 11);
        image.set(3, 2, 12, 12, 12);
        image.set(0, 3, 13, 13, 13);
        image.set(1, 3, 14, 14, 14);
        image.set(2, 3, 15, 15, 15);
        image.set(3, 3, 16, 16, 16);
        ColorImage expected = new ColorImage(4, 4);
        expected.set(2, 0, 1, 1, 1);
        expected.set(3, 0, 2, 2, 2);
        expected.set(0, 1, 3, 3, 3);
        expected.set(1, 1, 4, 4, 4);
        expected.set(2, 1, 5, 5, 5);
        expected.set(3, 1, 6, 6, 6);
        expected.set(0, 2, 7, 7, 7);
        expected.set(1, 2, 8, 8, 8);
        expected.set(2, 2, 9, 9, 9);
        expected.set(3, 2, 10, 10, 10);
        expected.set(0, 3, 11, 11, 11);
        expected.set(1, 3, 12, 12, 12);
        expected.set(2, 3, 13, 13, 13);
        expected.set(3, 3, 14, 14, 14);
        ColorImage result = shift.apply(image);
        assertEquals(expected, result);
    }
    
    //Test apply method for RealColorImage
    @Test
    public void testApply_RealColorImage() {
        RealColorImage image = new RealColorImage(4, 4);
        image.set(0, 0, 1.0f, 1.0f, 1.0f);
        image.set(1, 0, 2.0f, 2.0f, 2.0f);
        image.set(2, 0, 3.0f, 3.0f, 3.0f);
        image.set(3, 0, 4.0f, 4.0f, 4.0f);
        image.set(0, 1, 5.0f, 5.0f, 5.0f);
        image.set(1, 1, 6.0f, 6.0f, 6.0f);
        image.set(2, 1, 7.0f, 7.0f, 7.0f);
        image.set(3, 1, 8.0f, 8.0f, 8.0f);
        image.set(0, 2, 9.0f, 9.0f, 9.0f);
        image.set(1, 2, 10.0f, 10.0f, 10.0f);
        image.set(2, 2, 11.0f, 11.0f, 11.0f);
        image.set(3, 2, 12.0f, 12.0f, 12.0f);
        image.set(0, 3, 13.0f, 13.0f, 13.0f);
        image.set(1, 3, 14.0f, 14.0f, 14.0f);
        image.set(2, 3, 15.0f, 15.0f, 15.0f);
        image.set(3, 3, 16.0f, 16.0f, 16.0f);
        RealColorImage expected = new RealColorImage(4, 4);
        expected.set(2, 0, 1.0f, 1.0f, 1.0f);
        expected.set(3, 0, 2.0f, 2.0f, 2.0f);
        expected.set(0, 1, 3.0f, 3.0f, 3.0f);
        expected.set(1, 1, 4.0f, 4.0f, 4.0f);
        expected.set(2, 1, 5.0f, 5.0f, 5.0f);
        expected.set(3, 1, 6.0f, 6.0f, 6.0f);
        expected.set(0, 2, 7.0f, 7.0f, 7.0f);
        expected.set(1, 2, 8.0f, 8.0f, 8.0f);
        expected.set(2, 2, 9.0f, 9.0f, 9.0f);
        expected.set(3, 2, 10.0f, 10.0f, 10.0f);
        expected.set(0, 3, 11.0f, 11.0f, 11.0f);
        expected.set(1, 3, 12.0f, 12.0f, 12.0f);
        expected.set(2, 3, 13.0f, 13.0f, 13.0f);
        expected.set(3, 3, 14.0f, 14.0f, 14.0f);
        RealColorImage result = shift.apply(image);
        assertEquals(expected, result);
    }
}