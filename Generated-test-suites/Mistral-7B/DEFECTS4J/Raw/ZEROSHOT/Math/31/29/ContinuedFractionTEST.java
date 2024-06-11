import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Test of evaluate method, with default arguments.
     */
    @Test
    public void testEvaluateWithDefaultArguments() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.0);
        double expectedResult = 3.0;
        assertEquals(expectedResult, result, 0.0001);
    }

    /**
     * Test of evaluate method, with custom evaluation point.
     */
    @Test
    public void testEvaluateWithCustomEvaluationPoint() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(Math.PI, 1.0e-8, 100);
        double expectedResult = 1.6487212707001281;
        assertEquals(expectedResult, result, 1.0e-8);
    }

    /**
     * Test of evaluate method, with custom epsilon.
     */
    @Test
    public void testEvaluateWithCustomEpsilon() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.71828, 1.0e-12, 100);
        double expectedResult = 2.718281828459045;
        assertEquals(expectedResult, result, 1.0e-12);
    }

    /**
     * Test of evaluate method, with custom maxIterations.
     */
    @Test
    public void testEvaluateWithCustomMaxIterations() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        double result = continuedFraction.evaluate(1.4142135625, 1.0e-6, 5);
        double expectedResult = 1.648721270700126;
        assertEquals(expectedResult, result, 1.0e-6);
    }

    /**
     * Test of evaluate method, when the algorithm fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteDivergence() {
        ContinuedFraction continuedFraction = new MyInfiniteDivergenceContinuedFraction();
        continuedFraction.evaluate(0.0);
    }

    /**
     * Test of evaluate method, when the algorithm fails to converge due to a NaN result.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNResult() {
        ContinuedFraction continuedFraction = new MyNaNContinuedFraction();
        continuedFraction.evaluate(1.0, 1.0e-8, 100);
    }

    /**
     * Test of evaluate method, when the maximum number of convergents is exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxCountExceeded() {
        ContinuedFraction continuedFraction = new MySlowConvergingContinuedFraction();
        continuedFraction.evaluate(1.0, 1.0e-8, 1);
    }

    /**
     * Subclass for testing the {@link ContinuedFraction} class.
     */
    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return -2.0;
            }
            return 1.0;
        }

        @Override
        protected double getB(int n, double x) {
            if (n % 2 == 0) {
                return 3.0;
            }
            return 0.0;
        }
    }

    /**
     * Subclass for testing the infinite divergence behavior of the {@link ContinuedFraction} class.
     */
    private static class MyInfiniteDivergenceContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return 1.0;
        }

        @Override
        protected double getB(int n, double x) {
            if (n % 2 == 0) {
                return 0.0;
            }
            return -1.0;
        }
    }

    /**
     * Subclass for testing the NaN divergence behavior of the {@link ContinuedFraction} class.
     */
    private static class MyNaNContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n % 2 == 0) {
                return 1.0 / Math.sqrt(2.0);
            }
            return 1.0;
        }

        @Override
        protected double getB(int n, double x) {
            if (n % 2 == 0) {
                return -1.0 / Math.sqrt(2.0);
            }
            return 1.0;
        }
    }

    /**
     * Subclass for testing the slow converging behavior of the {@link ContinuedFraction} class.
     */
    private static class MySlowConvergingContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return Math.pow(2.0, n);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }
}