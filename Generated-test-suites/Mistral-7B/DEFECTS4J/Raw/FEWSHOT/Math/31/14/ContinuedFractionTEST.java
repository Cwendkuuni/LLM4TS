package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ContinuedFraction cf = new ContinuedFraction() {};
        try {
            cf.evaluate(0.0);
        } catch (Exception e) {
            // expected
        }
        try {
            cf.getA(0, 0.0);
        } catch (Exception e) {
            // expected
        }
        try {
            cf.getB(0, 0.0);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testEvaluateConstantCoefficients() {
        final ContinuedFraction sqrt2 = new Sqrt2ContinuedFraction();
        assertEquals(1.4142135623730955, sqrt2.evaluate(0.0), 1e-9);
    }

    @Test
    public void testEvaluateVariableCoefficients() {
        final ContinuedFraction sqrtX = new SqrtXContinuedFraction();
        assertEquals(1.5, sqrtX.evaluate(2.25), 1e-9);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateDivergence() {
        final ContinuedFraction log2 = new Log2ContinuedFraction();
        log2.evaluate(1.0, 1e-9, 1000);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceeded() {
        final ContinuedFraction pi = new PiContinuedFraction();
        pi.evaluate(0.0, 1e-9, 1);
    }

    @Test
    public void testEvaluateInfiniteDivergence() {
        final ContinuedFraction log1pX = new Log1pXContinuedFraction();
        assertNaN(log1pX.evaluate(-1.0, 1e-9, 1000));
    }

    @Test
    public void testEvaluateNaN() {
        final ContinuedFraction logX = new LogXContinuedFraction();
        assertNaN(logX.evaluate(0.0, 1e-9, 1000));
    }

    private static class Sqrt2ContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return n == 0 ? 2.0 : (n % 2 == 0 ? 1.0 : -1.0);
        }

        @Override
        protected double getB(int n, double x) {
            return n == 0 ? 2.0 : 2.0;
        }
    }

    private static class SqrtXContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return 1.0 / (x + 2.0 * n);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0 / (x + 2.0 * (n + 0.5));
        }
    }

    private static class Log2ContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return 1.0 / (2.0 * x + 2.0 * n + 1.0);
        }

        @Override
        protected double getB(int n, double x) {
            return (n + 1.0) / (2.0 * x + 2.0 * n + 2.0);
        }
    }

    private static class PiContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return -1.0 / ((2.0 * n + 1.0) * (2.0 * n + 1.0));
        }

        @Override
        protected double getB(int n, double x) {
            return (2.0 * n) / ((2.0 * n + 1.0) * (2.0 * n + 1.0));
        }
    }

    private static class Log1pXContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return (n + 1.0) / (x + n + 1.0);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0 / (x + n);
        }
    }

    private static class LogXContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            return 1.0 / (x + n);
        }

        @Override
        protected double getB(int n, double x) {
            return n / (x + n);
        }
    }
}