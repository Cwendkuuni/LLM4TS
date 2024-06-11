import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 10e-9;
    private ContinuedFraction continuedFraction;

    @Before
    public void setup() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
    }

    @Test
    public void testEvaluate() {
        double x = 1.0;
        double expectedValue = 1.0;
        double actualValue = continuedFraction.evaluate(x);

        assertEquals(expectedValue, actualValue, EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        double x = 1.0;
        double epsilon = 10e-6;
        double expectedValue = 1.0;
        double actualValue = continuedFraction.evaluate(x, epsilon);

        assertEquals(expectedValue, actualValue, epsilon);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        double x = 1.0;
        int maxIterations = 100;
        double expectedValue = 1.0;
        double actualValue = continuedFraction.evaluate(x, maxIterations);

        assertEquals(expectedValue, actualValue, EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        double x = 1.0;
        double epsilon = 10e-6;
        int maxIterations = 100;
        double expectedValue = 1.0;
        double actualValue = continuedFraction.evaluate(x, epsilon, maxIterations);

        assertEquals(expectedValue, actualValue, epsilon);
    }
}