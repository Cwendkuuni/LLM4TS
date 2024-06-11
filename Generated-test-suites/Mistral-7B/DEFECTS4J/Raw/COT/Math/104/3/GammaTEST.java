import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test valid inputs
        double result = Gamma.logGamma(1.5);
        assertEquals(Math.log(0.886226925452758), result, 1e-8);

        result = Gamma.logGamma(2.0);
        assertEquals(Math.log(0.918295834015273), result, 1e-8);

        result = Gamma.logGamma(3.0);
        assertEquals(Math.log(1.072687466964775), result, 1e-8);

        // Test edge cases and exceptions
        try {
            result = Gamma.logGamma(-1.0);
            fail("Expected MathException for negative input");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.logGamma(0.0);
            fail("Expected MathException for zero input");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.logGamma(1.5, 1.0e-12);
            fail("Expected MaxIterationsExceededException for small epsilon");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            result = Gamma.logGamma(1.5, Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException for negative maxIterations");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaP() {
        // Test valid inputs
        double result = Gamma.regularizedGammaP(1.5, 0.5);
        assertEquals(0.99283805357561, result, 1e-8);

        result = Gamma.regularizedGammaP(2.0, 2.0);
        assertEquals(0.63212055336333, result, 1e-8);

        result = Gamma.regularizedGammaP(3.0, 5.0);
        assertEquals(0.95671233225818, result, 1e-8);

        // Test edge cases and exceptions
        try {
            result = Gamma.regularizedGammaP(-1.0, 2.0);
            fail("Expected MathException for negative a");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaP(1.5, -2.0);
            fail("Expected MathException for negative x");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaP(0.0, 2.0);
            fail("Expected MathException for a<=0");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaP(1.5, 0.0);
            fail("Expected MathException for x<=0");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaP(1.5, 1.5, 1.0e-12, 1);
            fail("Expected MaxIterationsExceededException for small maxIterations");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaP(1.5, 1.5, 1, Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException for negative epsilon");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test valid inputs
        double result = Gamma.regularizedGammaQ(1.5, 0.5);
        assertEquals(0.007161946424244, result, 1e-8);

        result = Gamma.regularizedGammaQ(2.0, 2.0);
        assertEquals(0.36787944114225, result, 1e-8);

        result = Gamma.regularizedGammaQ(3.0, 5.0);
        assertEquals(0.04328766775182, result, 1e-8);

        // Test edge cases and exceptions
        try {
            result = Gamma.regularizedGammaQ(-1.0, 2.0);
            fail("Expected MathException for negative a");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.5, -2.0);
            fail("Expected MathException for negative x");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaQ(0.0, 2.0);
            fail("Expected MathException for a<=0");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.5, 0.0);
            fail("Expected MathException for x<=0");
        } catch (MathException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.5, 1.5, 1.0e-12, 1);
            fail("Expected MaxIterationsExceededException for small maxIterations");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            result = Gamma.regularizedGammaQ(1.5, 1.5, 1, Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException for negative epsilon");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}