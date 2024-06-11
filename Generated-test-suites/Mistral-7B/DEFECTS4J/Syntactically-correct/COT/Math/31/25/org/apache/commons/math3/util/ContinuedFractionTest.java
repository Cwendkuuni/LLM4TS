package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)} and {@link ContinuedFraction#getB(int, double)}.
     */
    @org.junit.Test
    public void testGetAAndB() {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;

        assertEquals(1.0, cf.getA(0, x), 0.0);
        assertEquals(1.0, cf.getA(1, x), 0.0);
        assertEquals(2.0, cf.getA(2, x), 0.0);
        assertEquals(1.0, cf.getB(0, x), 0.0);
        assertEquals(1.0, cf.getB(1, x), 0.0);
        assertEquals(2.0, cf.getB(2, x), 0.0);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)} with default epsilon and maxIterations.
     */
    @org.junit.Test
    public void testEvaluateDefault() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        double result = cf.evaluate(x);
        double expected = 2.0;
        assertEquals(expected, result, 0.0);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)} with default epsilon.
     */
    @org.junit.Test
    public void testEvaluateEpsilon() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-5;
        double result = cf.evaluate(x, epsilon);
        double expected = 2.0;
        assertEquals(expected, result, epsilon);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)} with default epsilon.
     */
    @org.junit.Test
    public void testEvaluateMaxIterations() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        int maxIterations = 5;
        double result = cf.evaluate(x, maxIterations);
        double expected = 2.0;
        assertEquals(expected, result, 0.0);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with given epsilon and maxIterations.
     */
    @org.junit.Test
    public void testEvaluateEpsilonMaxIterations() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-5;
        int maxIterations = 5;
        double result = cf.evaluate(x, epsilon, maxIterations);
        double expected = 2.0;
        assertEquals(expected, result, epsilon);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with epsilon smaller than
     * the actual difference between a and b coefficients to check convergence.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateSmallEpsilon() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-7;
        int maxIterations = Integer.MAX_VALUE;
        cf.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with maxIterations smaller than
     * required to converge.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsSmall() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-5;
        int maxIterations = 1;
        cf.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with infinite a or b coefficients.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteCoefficients() throws Exception {
        ContinuedFraction cf = new MyInfiniteCoefficientsContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-5;
        int maxIterations = Integer.MAX_VALUE;
        cf.evaluate(x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with NaN coefficients.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateNaNCoefficients() throws Exception {
        ContinuedFraction cf = new MyNaNContinuedFraction();
        double x = 1.0;
        double epsilon = 1e-5;
        int maxIterations = Integer.MAX_VALUE;
        cf.evaluate(x, epsilon, maxIterations);
    }
}

/**
 * Subclass of ContinuedFraction for testing purposes.
 */
class MyContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        } else {
            return 2.0;
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        } else {
            return 1.0;
        }
    }
}

/**
 * Subclass of ContinuedFraction for testing infinite a coefficients.
 */
class MyInfiniteCoefficientsContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    protected double getB(int n, double x) {
        return 1.0;
    }
}

/**
 * Subclass of ContinuedFraction for testing NaN coefficients.
 */
class MyNaNContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return Double.NaN;
    }

    @Override
    protected double getB(int n, double x) {
        return 1.0;
    }
}