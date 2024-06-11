package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;
    private static final int MAX_ITERATIONS = 10;

    @org.junit.Test
    public void testEvaluateWithDefaults() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0);
        assertEquals(1.6480517031032269, result, EPSILON);
    }

    @org.junit.Test
    public void testEvaluateWithCustomEpsilon() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, 1e-12);
        assertEquals(1.6480517031032269, result, 1e-12);
    }

    @org.junit.Test
    public void testEvaluateWithCustomMaxIterations() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(1.0, EPSILON, 5);
        assertEquals(1.6480517031, result, EPSILON);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateWithDefaultsThrowsException() {
        ContinuedFraction cf = new MyContinuedFractionThrowingException();
        cf.evaluate(0.0);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluateWithCustomMaxIterationsThrowsException() {
        ContinuedFraction cf = new MyContinuedFractionThrowingMaxIterationsException();
        cf.evaluate(1.0, EPSILON, 1);
    }

    @org.junit.Test
    public void testGetA() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(1.0, cf.getA(0, 0.0), EPSILON);
        assertEquals(2.0, cf.getA(1, 0.0), EPSILON);
        assertEquals(MathUtils.sqrt(2.0), cf.getA(1, 1.0), EPSILON);
    }

    @org.junit.Test
    public void testGetB() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(1.0, cf.getB(0, 0.0), EPSILON);
        assertEquals(-1.0, cf.getB(1, 0.0), EPSILON);
        assertEquals(MathUtils.sqrt(2.0), cf.getB(1, 1.0), EPSILON);
    }

    @org.junit.Test
    public void testEvaluateWithZeroArgument() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(0.0));
    }

    @org.junit.Test
    public void testEvaluateWithNegativeArgument() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(-1.0));
    }

    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return n == 0 ? 1.0 : (n % 2 == 0 ? -1.0 : 2.0 * x);
        }

        @Override
        protected double getB(int n, double x) {
            return n == 0 ? 1.0 : n % 2 == 0 ? x : 1.0;
        }
    }

    private static class MyContinuedFractionThrowingException extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }
    }

    private static class MyContinuedFractionThrowingMaxIterationsException extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return n > 0 ? 1.0 / FastMath.pow(2, n) : 0.0;
        }

        @Override
        protected double getB(int n, double x) {
            return n > 0 ? 1.0 / FastMath.pow(2, n) : 0.0;
        }

        @Override
        public double evaluate(double x, double epsilon, int maxIterations) {
            throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION,
                    maxIterations, x);
        }
    }

}