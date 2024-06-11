package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link ContinuedFraction}.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default arguments.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testEvaluateDefault() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double result = continuedFraction.evaluate(0.0);
        double expected = 2.0;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test evaluate method with specified epsilon.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testEvaluateEpsilon() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double epsilon = 1e-9;
        double result = continuedFraction.evaluate(0.0, epsilon);
        double expected = 2.0;
        assertEquals(expected, result, epsilon);
    }

    /**
     * Test evaluate method with specified maxIterations.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testEvaluateMaxIterations() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        int maxIterations = 5;
        double result = continuedFraction.evaluate(0.0, Integer.MAX_VALUE, maxIterations);
        double expected = 2.0;
        assertEquals(expected, result, 1e-6);
    }

    /**
     * Test evaluate method with infinite a and b values.
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteA() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with infinite b value.
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteB() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with NaN a or b values.
     * @throws Exception if an error occurs.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaN() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, Double.NaN, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with maxIterations exceeded.
     * @throws Exception if an error occurs.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterationsExceeded() throws Exception {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, Double.POSITIVE_INFINITY, 0);
    }

}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n == 0 ? 1.0 : (n & 1) == 0 ? -2.0 : 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return n == 0 ? 1.0 : 2.0;
    }
}