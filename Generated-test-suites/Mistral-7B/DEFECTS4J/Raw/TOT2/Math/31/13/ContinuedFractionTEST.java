import junit.framework.TestCase;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;

public class ContinuedFractionTest1 extends TestCase {

    private ContinuedFraction continuedFraction = new ContinuedFraction() {
        @Override
        protected double getA(int n, double x) {
            return n == 0 ? 1.0 : x;
        }

        @Override
        protected double getB(int n, double x) {
            return n == 0 ? 1.0 : (x > 0 ? -1.0 : 1.0);
        }
    };

    public void testEvaluateWithDefaultParameters() {
        double result = continuedFraction.evaluate(1.0);
        assertEquals(2.0, result, 0.0001);
    }

    public void testEvaluateWithGivenEpsilon() {
        double result = continuedFraction.evaluate(1.0, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    public void testEvaluateWithGivenMaxIterations() {
        try {
            continuedFraction.evaluate(1.0, 1, 0);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    public void testEvaluateWithInfinity() {
        try {
            continuedFraction.evaluate(Double.POSITIVE_INFINITY);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    public void testEvaluateWithNaN() {
        try {
            continuedFraction.evaluate(Double.NaN);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}