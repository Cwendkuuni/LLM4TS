package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

/**
 * Test class for ContinuedFraction class.
 */
public class ContinuedFractionTest {
    /**
     * Test evaluate method with default epsilon and maxIterations.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testEvaluateDefault() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0);
        double expectedResult = 3.0;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test evaluate method with custom epsilon.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testEvaluateCustomEpsilon() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, 1e-12);
        double expectedResult = 3.0;
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test evaluate method with custom maxIterations.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test
    public void testEvaluateCustomMaxIterations() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, 10);
        double expectedResult = 3.0;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test evaluate method with infinite value for epsilon.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateInfEpsilon() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with negative value for epsilon.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluateNegativeEpsilon() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, -1.0);
    }

    /**
     * Test evaluate method with negative value for x.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluateNegativeX() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(-1.0, 0.0, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with maxIterations = 0.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluateMaxIterationsZero() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, 0, 0);
    }

    /**
     * Test evaluate method with maxIterations < 0.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEvaluateMaxIterationsNegative() throws Exception {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(1.0, 0, -1);
    }

    /**
     * Test evaluate method with non-convergent continued fraction.
     * @throws Exception if an error occurs.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateNonConvergent() throws Exception {
        ContinuedFraction cf = new MyNonConvergentContinuedFraction();
        cf.evaluate(1.0, 10, 10);
    }

}

class MyContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return (double) n / (double) (n + 1);
    }

    @Override
    protected double getB(int n, double x) {
        return 1.0;
    }
}

class MyNonConvergentContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return (double) Math.pow(2.0, n);
    }

    @Override
    protected double getB(int n, double x) {
        return (double) (n + 1);
    }
}