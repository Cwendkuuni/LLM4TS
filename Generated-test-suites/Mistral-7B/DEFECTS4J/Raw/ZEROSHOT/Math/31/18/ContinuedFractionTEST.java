import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.math.BigDecimal;
import org.apache.commons.math3.util.math.MathConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ContinuedFractionTest {

    private ContinuedFraction continuedFraction;
    private final double EPSILON = 1e-9;
    private final double MAX_ITERATIONS = 100;

    @Before
    public void setUp() {
        continuedFraction = new MyContinuedFraction();
    }

    @After
    public void tearDown() {
        continuedFraction = null;
    }

    @Test
    public void testEvaluateSquareRootOfTwo() {
        double result = continuedFraction.evaluate(Math.sqrt(2));
        assertEquals(Math.sqrt(2), result, EPSILON);
    }

    @Test
    public void testEvaluateE() {
        double result = continuedFraction.evaluate(Math.E);
        assertEquals(Math.E, result, EPSILON);
    }

    @Test
    public void testEvaluatePi() {
        double result = continuedFraction.evaluate(MathConstants.PI);
        assertEquals(MathConstants.PI, result, EPSILON);
    }

    @Test
    public void testEvaluateZero() {
        double result = continuedFraction.evaluate(0.0);
        assertEquals(0.0, result, EPSILON);
    }

    @Test
    public void testEvaluateNegativeNumber() {
        double result = continuedFraction.evaluate(-1.0);
        assertEquals(-1.0, result, EPSILON);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        double customEpsilon = 1e-12;
        double result = continuedFraction.evaluate(Math.sqrt(2), customEpsilon, MAX_ITERATIONS);
        assertEquals(Math.sqrt(2), result, customEpsilon);
    }

    @Test
    public void testEvaluateWithCustomMaxIterations() {
        int customMaxIterations = 5;
        double result = continuedFraction.evaluate(Math.sqrt(2), EPSILON, customMaxIterations);
        assertEquals(Math.sqrt(2), result, EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateDivergenceExceptionWithDefaultEpsilonAndMaxIterations() {
        continuedFraction.evaluate(1.0 / 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateDivergenceExceptionWithCustomEpsilonAndMaxIterations() {
        continuedFraction.evaluate(1.0 / 0.0, 1e-11, 4);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        continuedFraction.evaluate(Math.sqrt(2), EPSILON, 0);
    }

    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 1.0;
            } else if (n % 2 == 0) {
                return 1.0 / (2.0 * x);
            } else {
                return 2.0 * x;
            }
        }

        @Override
        protected double getB(int n, double x) {
            if (n == 0) {
                return 1.0;
            } else if (n % 2 == 0) {
                return (n + 1.0) / (2.0 * x);
            } else {
                return -x;
            }
        }
    }
}