package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

public class ContinuedFractionTest {

    private ContinuedFraction continuedFraction = new ContinuedFraction() {
        @Override
        protected double getA(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return x;
                default:
                    throw new UnsupportedOperationException("Method getA not implemented.");
            }
        }

        @Override
        protected double getB(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return -1.0;
                default:
                    throw new UnsupportedOperationException("Method getB not implemented.");
            }
        }
    };

    @org.junit.Test
    public void testEvaluate() {
        double x = 1.0;
        double result = continuedFraction.evaluate(x);
        assertEquals(2.0, result, 1e-8);
    }

    @org.junit.Test
    public void testEvaluate_epsilon() {
        double x = 1.0;
        double epsilon = 1e-9;
        double result = continuedFraction.evaluate(x, epsilon);
        assertEquals(2.0, result, epsilon);
    }

    @org.junit.Test
    public void testEvaluate_maxIterations() {
        double x = 1.0;
        int maxIterations = 5;
        double result = continuedFraction.evaluate(x, maxIterations);
        assertEquals(1.5, result, 1e-6);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluate_convergenceException() {
        double x = 0.0;
        double epsilon = 1e-10;
        continuedFraction.evaluate(x, epsilon, 10);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testEvaluate_maxCountExceededException() {
        double x = 1.0;
        int maxIterations = 1;
        continuedFraction.evaluate(x, 1e-9, maxIterations);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluate_infiniteDivergence() {
        double x = 1.0 / 0.0;
        double epsilon = 1e-9;
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(x, epsilon, 10));
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluate_nanDivergence() {
        double x = Double.NaN;
        double epsilon = 1e-9;
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(x, epsilon, 10));
    }

    @org.junit.Test
    public void testEvaluate_largeArg() {
        double x = 1e308;
        double epsilon = 1e-308;
        double result = continuedFraction.evaluate(x, epsilon, 100);
        assertEquals(FastMath.E, result, epsilon);
    }
}