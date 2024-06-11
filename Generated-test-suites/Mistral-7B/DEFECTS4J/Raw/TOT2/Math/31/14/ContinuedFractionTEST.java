
package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(0.0);
        assertEquals(expectedValue, result, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithMinValue() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(Double.MIN_VALUE, 0.0, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithMaxValue() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(Double.MAX_VALUE, 0.0, 10);
    }

    @Test
    public void testEvaluateWithNaN() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(Double.NaN, 0.0, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testEvaluateWithInfinity() {
        ContinuedFraction cf = new MyContinuedFraction();
        try {
            cf.evaluate(Double.POSITIVE_INFINITY, 0.0, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            cf.evaluate(Double.NEGATIVE_INFINITY, 0.0, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithZeroEpsilon() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(0.1, 0.0, 10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithMaxIterationsZero() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate(0.1, 0.0, 0);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateWithInfiniteCoefficients() {
        ContinuedFraction cf = new MyContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            protected double getB(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }
        };
        try {
            cf.evaluate(0.1, 0.0, 10);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}
