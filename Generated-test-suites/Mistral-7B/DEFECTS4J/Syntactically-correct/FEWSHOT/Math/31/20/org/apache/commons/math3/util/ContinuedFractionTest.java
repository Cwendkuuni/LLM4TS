package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
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
        try {
            cf.evaluate(0.0);
            fail("Expected UnsupportedOperationException");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithDefaults() {
        ContinuedFraction sqrt2 = new SqrtContinuedFraction();
        assertEquals(1.4142135623730955, sqrt2.evaluate(2.0), 1e-7);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        ContinuedFraction sqrt2 = new SqrtContinuedFraction();
        assertEquals(1.4142135623730955, sqrt2.evaluate(2.0, 1e-10), 1e-11);
    }

    @Test
    public void testEvaluateWithCustomMaxIterations() {
        ContinuedFraction sqrt2 = new SqrtContinuedFraction();
        assertEquals(1.4142135623730955, sqrt2.evaluate(2.0, 10), 1e-7);
    }

    @Test(expected = ConvergenceException.class)
    public void testDivergenceException() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        log2.evaluate(1.0, 1e-20, 10000);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        ContinuedFraction log2 = new Log2ContinuedFraction();
        log2.evaluate(1.0, 1e-10, 1);
    }
}

class SqrtContinuedFraction extends ContinuedFraction {
    protected double getA(int n, double x) {
        return (x + 1.0) / (x + n * n);
    }

    protected double getB(int n, double x) {
        return -2.0 * x / (x + n * n);
    }
}

class Log2ContinuedFraction extends ContinuedFraction {
    protected double getA(int n, double x) {
        return (1.0 + x) / (n + x);
    }

    protected double getB(int n, double x) {
        return 1.0 / (n + x);
    }
}