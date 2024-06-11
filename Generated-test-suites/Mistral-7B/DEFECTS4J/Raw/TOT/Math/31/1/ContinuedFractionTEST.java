package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ContinuedFraction} abstract class.
 */
public class ContinuedFractionTest {

    /**
     * Test the evaluate method with a simple continued fraction.
     */
    @Test
    public void testEvaluateSimple() {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        double result = continuedFraction.evaluate(1.0);
        double expected = 2.0;
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test the evaluate method with a convergent continued fraction.
     */
    @Test
    public void testEvaluateConvergent() {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        double result = continuedFraction.evaluate(2.0);
        double expected = 1.4142135623730955;
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test the evaluate method with a divergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateDivergent() {
        ContinuedFraction continuedFraction = new LogarithmBase2ContinuedFraction();
        continuedFraction.evaluate(0.0);
    }

    /**
     * Test the evaluate method with a maximum iterations reached continued fraction.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction continuedFraction = new LogarithmBaseEContinuedFraction();
        continuedFraction.evaluate(1.0, 0.0, 1);
    }

    /**
     * Test the evaluate method with a very small epsilon.
     */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction continuedFraction = new SinContinuedFraction();
        double result = continuedFraction.evaluate(0.0, 1e-16, 1000);
        double expected = 0.0;
        assertEquals(expected, result, 1e-16);
    }

    /**
     * Test the evaluate method with a negative argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNegativeArgument() {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        continuedFraction.evaluate(-1.0);
    }

}