import static org.junit.Assert.*;
import static org.apache.commons.math3.special.Gamma.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test for positive values
        assertEquals(Math.log(0.5), logGamma(0.5), 1e-7);
        assertEquals(Math.log(1.0), logGamma(1.0), 1e-14);
        assertEquals(Math.log(2.0), logGamma(2.0), 1e-14);

        // Test for negative values and complex numbers, which should throw an exception
        try {
            logGamma(-1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }

        try {
            logGamma(new Complex(1.0, 2.0));
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        // Test for positive values
        assertEquals(0.886226925452758, regularizedGammaP(1.2, 1.0), 1e-7);
        assertEquals(1.0, regularizedGammaP(1.0, 1.0), 1e-14);
        assertEquals(0.5772156649015329, regularizedGammaP(0.5, 1.0), 1e-7);

        // Test for non-positive values, which should throw an exception
        try {
            regularizedGammaP(1.0, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test for positive values
        assertEquals(0.113775825412352, 1.0 - regularizedGammaP(1.2, 1.0), 1e-7);
        assertEquals(0.5, 1.0 - regularizedGammaP(1.0, 1.0), 1e-14);
        assertEquals(0.422784335070638, 1.0 - regularizedGammaP(0.5, 1.0), 1e-7);

        // Test for non-positive values, which should throw an exception
        try {
            regularizedGammaQ(1.0, -1.0);
            fail("Expected MathException");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testMaxIterationsExceededException() {
        try {
            regularizedGammaP(0.5, 1.0, 1e-20, 1);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }
}