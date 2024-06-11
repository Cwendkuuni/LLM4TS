package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 100;

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteA() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(1.0, EPSILON, MAX_ITERATIONS);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteB() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(1.0, EPSILON, MAX_ITERATIONS);

        continuedFraction = new TestContinuedFractionWithBInfinity();
        continuedFraction.evaluate(1.0, EPSILON, MAX_ITERATIONS);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithNaNB() {
        ContinuedFraction continuedFraction = new TestContinuedFractionWithBNan();
        continuedFraction.evaluate(1.0, EPSILON, MAX_ITERATIONS);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(1.0, EPSILON, 0);
    }

    @Test
    public void testEvaluateSimpleContinuedFraction() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double value = continuedFraction.evaluate(1.0);
        assertEquals(Math.sqrt(2.0), value, EPSILON);
    }

    @Test
    public void testEvaluateComplexContinuedFraction() {
        ContinuedFraction continuedFraction = new TestComplexContinuedFraction();
        double value = continuedFraction.evaluate(1.0);
        assertTrue(FastMath.abs(FastMath.PI / 2 - value) < EPSILON);
    }
}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n == 0) {
            return 1.0;
        }
        return -1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return 2.0;
    }
}

class TestContinuedFractionWithBInfinity extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        if (n == 0) {
            return 1.0;
        }
        return Double.POSITIVE_INFINITY;
    }
}

class TestContinuedFractionWithBNan extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        if (n == 0) {
            return 1.0;
        }
        return Double.NaN;
    }
}

class TestComplexContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        }
        return 0.0;
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return 1.0 / (2.0 * n + 1.0);
        }
        return -1.0 / (2.0 * n + 1.0);
    }
}