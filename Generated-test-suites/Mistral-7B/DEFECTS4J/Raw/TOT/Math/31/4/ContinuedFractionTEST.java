package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    /**
     * Tests evaluation of a simple continued fraction.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws MaxCountExceededException if the maximum number of iterations is exceeded.
     */
    @Test
    public void testSimpleContinuedFraction() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new SimpleContinuedFraction();
        double result = continuedFraction.evaluate(1.0, 1e-10, 1000);
        Assert.assertEquals(Math.PI / 4.0, result, 1e-11);
    }

    /**
     * Tests evaluation of a continued fraction with infinite divergence.
     * @throws ConvergenceException if the algorithm fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testInfiniteDivergence() throws ConvergenceException {
        ContinuedFraction continuedFraction = new SqrtContinuedFraction();
        continuedFraction.evaluate(1.0, 1e-10, 1000);
    }

    /**
     * Tests evaluation of a continued fraction with NaN divergence.
     * @throws ConvergenceException if the algorithm fails to converge.
     */
    @Test(expected = ConvergenceException.class)
    public void testNaNdivergence() throws ConvergenceException {
        ContinuedFraction continuedFraction = new CotangentContinuedFraction();
        continuedFraction.evaluate(0.0, 1e-10, 1000);
    }

    /**
     * Tests evaluation of a continued fraction with a maximum number of iterations exceeded.
     * @throws ConvergenceException if the algorithm fails to converge.
     * @throws MaxCountExceededException if the maximum number of iterations is exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded() throws ConvergenceException, MaxCountExceededException {
        ContinuedFraction continuedFraction = new PiContinuedFraction();
        continuedFraction.evaluate(0.0, 1e-10, 1);
    }

}