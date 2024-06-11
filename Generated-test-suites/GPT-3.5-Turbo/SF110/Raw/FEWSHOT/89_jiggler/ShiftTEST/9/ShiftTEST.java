package jigl.image.ops;

import jigl.image.exceptions.*;
import jigl.image.ops.Shift;
import jigl.image.types.*;
import jigl.image.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
    @Test
    public void testApplyGrayImage() {
        // normal case
        GrayImage grayImage = new GrayImage(100, 100);
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        GrayImage result = shift.apply(grayImage);
        // verify result
        
        // edge cases
        // ...
    }

    @Test
    public void testApplyRealGrayImage() {
        // normal case
        RealGrayImage realGrayImage = new RealGrayImage(100, 100);
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        RealGrayImage result = shift.apply(realGrayImage);
        // verify result
        
        // edge cases
        // ...
    }

    @Test
    public void testApplyColorImage() {
        // normal case
        ColorImage colorImage = new ColorImage(100, 100);
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        ColorImage result = shift.apply(colorImage);
        // verify result
        
        // edge cases
        // ...
    }

    @Test
    public void testApplyRealColorImage() {
        // normal case
        RealColorImage realColorImage = new RealColorImage(100, 100);
        Shift shift = new Shift(10, 10, Shift.NOWRAP);
        RealColorImage result = shift.apply(realColorImage);
        // verify result
        
        // edge cases
        // ...
    }
}