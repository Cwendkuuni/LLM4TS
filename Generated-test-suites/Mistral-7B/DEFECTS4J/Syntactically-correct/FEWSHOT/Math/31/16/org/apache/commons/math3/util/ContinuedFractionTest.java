package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };

        cf.evaluate(0.0);
    }

    @Test
    public void testEvaluateWithDefaults() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(2.0, cfsqrt.evaluate(2.0), 0.0);
        assertEquals(1.5, cfsqrt.evaluate(1.5), 0.0);
        assertEquals(Math.sqrt(2), cfsqrt.evaluate(1.0), 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(2.0, cfsqrt.evaluate(2.0, 1e-12), 1e-12);
        assertEquals(1.5, cfsqrt.evaluate(1.5, 1e-12), 1e-12);
        assertEquals(Math.sqrt(2), cfsqrt.evaluate(1.0, 1e-12), 1e-12);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(2.0, cfsqrt.evaluate(2.0, Integer.MAX_VALUE), 0.0);
        assertEquals(1.5, cfsqrt.evaluate(1.5, Integer.MAX_VALUE), 0.0);
        assertEquals(Math.sqrt(2), cfsqrt.evaluate(1.0, Integer.MAX_VALUE), 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        cfsqrt.evaluate(0.0, 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        cfsqrt.evaluate(0.0, 10000);
    }

    @Test
    public void testGetA() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(2.0, cfsqrt.getA(0, 2.0), 0.0);
        assertEquals(1.5, cfsqrt.getA(0, 1.5), 0.0);
        assertEquals(Math.sqrt(2), cfsqrt.getA(0, 1.0), 0.0);
        assertEquals(1.0, cfsqrt.getA(1, 2.0), 0.0);
        assertEquals(2.5, cfsqrt.getA(1, 1.5), 0.0);
        assertEquals(1.75, cfsqrt.getA(1, 1.25), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(1.0, cfsqrt.getB(0, 2.0), 0.0);
        assertEquals(1.0, cfsqrt.getB(0, 1.5), 0.0);
        assertEquals(1.0 / 2, cfsqrt.getB(0, 1.0), 0.0);
        assertEquals(1.0, cfsqrt.getB(1, 2.0), 0.0);
        assertEquals(1.0, cfsqrt.getB(1, 1.5), 0.0);
        assertEquals(2.0 / 3, cfsqrt.getB(1, 1.25), 0.0);
    }

    @Test
    public void testEvaluateWithDifferentEpsilonAndMaxIterations() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        double epsilon = 1e-12;
        double maxIterations = 100;
        assertEquals(Math.sqrt(2), cfsqrt.evaluate(1.0, epsilon, maxIterations), epsilon);
        assertEquals(Math.sqrt(2), cfsqrt.evaluate(1.0, epsilon, maxIterations * 2), epsilon);
    }

    @Test
    public void testEvaluateWithZeroX() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(0.0, cfsqrt.evaluate(0.0), 0.0);
    }

    @Test
    public void testEvaluateWithNegativeX() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertEquals(-1.0, cfsqrt.evaluate(-1.0), 0.0);
    }

    @Test
    public void testEvaluateWithInfX() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertNull(cfsqrt.evaluate(Double.POSITIVE_INFINITY));
    }

    @Test
    public void testEvaluateWithNaNX() {
        ContinuedFractionSqrtTest cfsqrt = new ContinuedFractionSqrtTest();
        assertNull(cfsqrt.evaluate(Double.NaN));
    }

    @Test
    public void testLentzAlgorithm() {
        ContinuedFractionGoldenRatioTest cfgoldenRatio = new ContinuedFractionGoldenRatioTest();
        assertEquals(1.618051251196528, cfgoldenRatio.evaluate(0.0, 1e-12), 1e-12);
        assertEquals(1.618051251196528, cfgoldenRatio.evaluate(1.0, 1e-12), 1e-12);
        assertEquals(1.618051251196528, cfgoldenRatio.evaluate(2.0, 1e-12), 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testLentzAlgorithmFailure() {
        ContinuedFractionGoldenRatioTest cfgoldenRatio = new ContinuedFractionGoldenRatioTest();
        cfgoldenRatio.evaluate(-1.0, 1e-12);
    }

    private static class ContinuedFractionSqrtTest extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return x;
                default:
                    return 1.0 - (x * (x - 1.0) / ((2.0 * n + 1.0) * (2.0 * n + 1.0)));
            }
        }

        @Override
        protected double getB(int n, double x) {
            switch (n) {
                case 0:
                    return 0.0;
                case 1:
                    return 1.0;
                default:
                    return 1.0 - (x * (x - 1.0) / ((2.0 * n) * (2.0 * n)));
            }
        }
    }

    private static class ContinuedFractionGoldenRatioTest extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            switch (n) {
                case 0:
                    return 1.0;
                case 1:
                    return (1.0 + 1.0) / 2.0;
                default:
                    return getA(n - 1, x) + getB(n - 1, x);
            }
        }

        @Override
        protected double getB(int n, double x) {
            switch (n) {
                case 0:
                    return 0.0;
                case 1:
                    return 1.0;
                default:
                    return getA(n - 1, x);
            }
        }
    }
}