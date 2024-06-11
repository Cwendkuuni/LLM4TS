package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.special.Fraction;
import org.junit.Test;

public class ContinuedFractionTest {

    /**
     * Test evaluate method with simple continued fraction: 1 + 1/2 + 1/4 + ...
     */
    @Test
    public void testSquareRoot() {
        SquareRootContinuedFraction sqrt = new SquareRootContinuedFraction();
        double epsilon = 1.0e-15;
        double x = 2.0;
        double result = sqrt.evaluate(x, epsilon);
        Fraction expected = new Fraction(Math.sqrt(x), 1);
        assertEquals(expected.getNumerator(), result, epsilon);
        assertEquals(expected.getDenominator(), 1, epsilon);
    }

    /**
     * Test evaluate method with simple continued fraction: 1 + 1/3 + 1/3^2 + ...
     */
    @Test
    public void testCubicRoot() {
        CubicRootContinuedFraction cubicRoot = new CubicRootContinuedFraction();
        double epsilon = 1.0e-15;
        double x = 8.0;
        double result = cubicRoot.evaluate(x, epsilon);
        Fraction expected = new Fraction(Math.cbrt(x), 1);
        assertEquals(expected.getNumerator(), result, epsilon);
        assertEquals(expected.getDenominator(), 1, epsilon);
    }

    /**
     * Test evaluate method with non-converging continued fraction: 1 + 1/2 + 1/4 + 1/8 + ...
     */
    @Test(expected = MaxCountExceededException.class)
    public void testNonConverging() {
        SquareRootContinuedFraction sqrt = new SquareRootContinuedFraction();
        double x = 2.0;
        sqrt.evaluate(x, 1.0e-15, 10000);
    }

    /**
     * Test evaluate method with diverging continued fraction: 1 + 1/2 + 1/2^2 + 1/2^3 + ...
     */
    @Test(expected = ConvergenceException.class)
    public void testDiverging() {
        SquareRootContinuedFraction sqrt = new SquareRootContinuedFraction();
        double x = 2.0;
        sqrt.evaluate(x, 1.0e-15, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with infinite continued fraction: 1 + 1/1 + 1/1 + ...
     */
    @Test(expected = ConvergenceException.class)
    public void testInfinite() {
        InfiniteContinuedFraction infinite = new InfiniteContinuedFraction();
        double x = 1.0;
        infinite.evaluate(x, 1.0e-15, Integer.MAX_VALUE);
    }

    /**
     * Test evaluate method with zero continued fraction: 0 + 0 + 0 + ...
     */
    @Test
    public void testZero() {
        ZeroContinuedFraction zero = new ZeroContinuedFraction();
        double x = 0.0;
        double result = zero.evaluate(x, 1.0e-15);
        assertEquals(0.0, result, 1.0e-15);
    }

}