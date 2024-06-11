package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Test for evaluate method with default parameters.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new TestContinuedFraction();
        try {
            double result = cf.evaluate(Math.PI);
            Assert.assertEquals("Incorrect value for Pi.", Math.PI, result, 1e-10);
        } catch (ConvergenceException e) {
            Assert.fail("Unexpected ConvergenceException.");
        }
    }

    /**
     * Test for evaluate method with epsilon parameter.
     */
    @Test
    public void testEvaluateEpsilon() {
        ContinuedFraction cf = new TestContinuedFraction();
        try {
            double result = cf.evaluate(Math.PI, 1e-15);
            Assert.assertEquals("Incorrect value for Pi with epsilon=1e-15.", Math.PI, result, 1e-15);
        } catch (ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception.");
        }
    }

    /**
     * Test for evaluate method with maxIterations parameter.
     */
    @Test
    public void testEvaluateMaxIterations() {
        ContinuedFraction cf = new TestContinuedFraction();
        try {
            double result = cf.evaluate(Math.E, 10);
            Assert.assertEquals("Incorrect value for E with maxIterations=10.", Math.E, result, 1e-10);
        } catch (ConvergenceException | MaxCountExceededException e) {
            Assert.fail("Unexpected exception.");
        }
    }

    /**
     * Test for evaluate method with infinite values.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateInfinite() {
        ContinuedFraction cf = new TestContinuedFraction();
        cf.evaluate(Double.POSITIVE_INFINITY, 0.0, 10);
    }

    /**
     * Test for evaluate method with NaN values.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateNaN() {
        ContinuedFraction cf = new TestContinuedFraction();
        cf.evaluate(Double.NaN, 0.0, 10);
    }

    /**
     * Test for evaluate method with negative x.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateNegativeX() {
        ContinuedFraction cf = new TestContinuedFraction();
        cf.evaluate(-1.0);
    }
}

/**
 * A test class for {@link ContinuedFraction} abstract class.
 */
class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
            case 0:
                return 1.0;
            case 1:
                return x;
            default:
                throw new UnsupportedOperationException("Unsupported n value: " + n);
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
            case 0:
                return 1.0;
            case 1:
                return 1.0;
            default:
                throw new UnsupportedOperationException("Unsupported n value: " + n);
        }
    }
}