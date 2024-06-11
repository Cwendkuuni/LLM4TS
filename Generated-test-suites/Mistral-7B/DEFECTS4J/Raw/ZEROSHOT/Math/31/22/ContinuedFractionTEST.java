import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for ContinuedFraction class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default arguments.
     */
    @Test
    public void testEvaluateWithDefaultArguments() {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        assertEquals("Evaluate method should return 1.5 for square root of 2", 1.5, result, 1e-8);
    }

    /**
     * Test evaluate method with custom epsilon.
     */
    @Test
    public void testEvaluateWithCustomEpsilon() {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 1e-12);
        assertEquals("Evaluate method should return 1.5 for square root of 2 with custom epsilon", 1.5, result, 1e-13);
    }

    /**
     * Test evaluate method with custom maxIterations.
     */
    @Test
    public void testEvaluateWithCustomMaxIterations() {
        ContinuedFraction continuedFraction = new SquareRootContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 10);
        assertEquals("Evaluate method should return a convergent value for square root of 2 with custom maxIterations",
                     isClose(1.5), result, 1e-8);
    }

    /**
     * Test evaluate method with non-convergent continued fraction.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithNonConvergentContinuedFraction() {
        ContinuedFraction continuedFraction = new SquareRootContinuedFractionWithNonConvergence();
        continuedFraction.evaluate(2.0, 100);
    }

    /**
     * Test evaluate method with infinite continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteContinuedFraction() {
        ContinuedFraction continuedFraction = new LogarithmBase2ContinuedFraction();
        continuedFraction.evaluate(1.0);
    }

    /**
     * Test evaluate method with NaN continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNContinuedFraction() {
        ContinuedFraction continuedFraction = new LogarithmBaseNegativeOneContinuedFraction();
        continuedFraction.evaluate(2.0);
    }

    /**
     * Helper method for comparing doubles with a given precision.
     *
     * @param expected the expected value.
     * @param actual the actual value.
     * @param precision the precision.
     * @return true if the difference between expected and actual is smaller than the precision, false otherwise.
     */
    private boolean isClose(double expected) {
        return isClose(expected, 0.0, DEFAULT_EPSILON);
    }

    /**
     * Helper method for comparing doubles with a given precision and tolerance.
     *
     * @param expected the expected value.
     * @param actual the actual value.
     * @param precision the precision.
     * @return true if the difference between expected and actual is smaller than the precision, false otherwise.
     */
    private boolean isClose(double expected, double actual, double precision) {
        return Precision.isClose(expected, actual, precision);
    }
}