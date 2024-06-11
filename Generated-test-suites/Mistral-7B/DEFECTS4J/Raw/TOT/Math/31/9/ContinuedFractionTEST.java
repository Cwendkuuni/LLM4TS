package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the ContinuedFraction abstract class.
 */
public class ContinuedFractionTest {

    /**
     * Test the evaluate method with a simple continued fraction.
     */
    @Test
    public void testSquareRoot() {
        ContinuedFraction sqrtCF = new SquareRootContinuedFraction();
        double epsilon = 1e-5;
        double x = 2.0;
        double result = sqrtCF.evaluate(x, epsilon, Integer.MAX_VALUE);
        double expected = Math.sqrt(x);
        Assert.assertEquals(expected, result, epsilon);
    }

    /**
     * Test the evaluate method with a convergent continued fraction.
     */
    @Test
    public void testEuler() {
        ContinuedFraction eulerCF = new EulerContinuedFraction();
        double epsilon = 1e-9;
        double x = Math.PI;
        double result = eulerCF.evaluate(x, epsilon, Integer.MAX_VALUE);
        double expected = Math.E;
        Assert.assertEquals(expected, result, epsilon);
    }

    /**
     * Test the evaluate method with non-convergent continued fraction.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testNonConvergent() {
        ContinuedFraction sqrtNegativeCF = new SquareRootNegativeContinuedFraction();
        double epsilon = 1e-5;
        double x = -1.0;
        sqrtNegativeCF.evaluate(x, epsilon, 10);
    }

    /**
     * Test the evaluate method with an infinite divergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testInfinityDivergent() {
        ContinuedFraction log2CF = new Log2ContinuedFraction();
        double epsilon = 1e-5;
        double x = 1.0;
        log2CF.evaluate(x, epsilon, Integer.MAX_VALUE);
    }

    /**
     * Test the evaluate method with a NaN divergent continued fraction.
     */
    @Test(expected = ConvergenceException.class)
    public void testNaNdivergent() {
        ContinuedFraction log1CF = new Log1ContinuedFraction();
        double epsilon = 1e-5;
        double x = 0.0;
        log1CF.evaluate(x, epsilon, Integer.MAX_VALUE);
    }

}