package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for ContinuedFraction abstract class.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default arguments.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        double expected = 1.4142135623730955;
        Assert.assertEquals(expected, result, 1e-9);
    }

    /**
     * Test evaluate method with custom epsilon value.
     */
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 1e-12);
        double expected = 1.4142137410144066;
        Assert.assertEquals(expected, result, 1e-12);
    }

    /**
     * Test evaluate method with custom maxIterations value.
     */
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0, 10);
        double expected = 1.4142139459538535;
        Assert.assertEquals(expected, result, 1e-9);
    }

    /**
     * Test evaluate method with infinite value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteValue() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with NaN value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaNValue() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(Double.NaN);
    }

    /**
     * Test evaluate method with negative value.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNegativeValue() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(-2.0);
    }

    /**
     * Test evaluate method with epsilon smaller than machine epsilon.
     */
    @Test
    public void testEvaluateMachineEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double epsilon = 1e-324;
        double result = continuedFraction.evaluate(2.0, epsilon);
        Assert.assertEquals(1.0, result, epsilon);
    }

    /**
     * Test evaluate method with maxIterations equals to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateZeroMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(2.0, 0);
    }

    /**
     * Test evaluate method with maxIterations equals to negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNegativeMaxIterations() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(2.0, -1);
    }

    /**
     * Test evaluate method with invalid epsilon value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateInvalidEpsilon() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(2.0, 0.0);
    }

    /**
     * Test evaluate method with invalid x value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateInvalidX() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(null, 1e-9);
    }

    /**
     * Test evaluate method with big value.
     */
    @Test
    public void testEvaluateBigValue() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(1e100, 1e-9);
        double expected = 3.1622776601683795E100;
        Assert.assertEquals(expected, result, 1e-9);
    }
}