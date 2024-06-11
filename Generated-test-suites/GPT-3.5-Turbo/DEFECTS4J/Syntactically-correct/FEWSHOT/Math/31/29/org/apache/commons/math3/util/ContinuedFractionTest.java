package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ContinuedFractionTest {
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(cf.getA(0, 1.0), 1.0, 0.0);
        assertEquals(cf.getA(1, 2.0), 3.0, 0.0);
        assertEquals(cf.getA(2, 3.0), 5.0, 0.0);
        assertEquals(cf.getA(3, 4.0), 7.0, 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n + x;
            }
        };

        assertEquals(cf.getB(0, 1.0), 1.0, 0.0);
        assertEquals(cf.getB(1, 2.0), 3.0, 0.0);
        assertEquals(cf.getB(2, 3.0), 4.0, 0.0);
        assertEquals(cf.getB(3, 4.0), 7.0, 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n + x;
            }
        };

        assertEquals(cf.evaluate(0.0), 0.0, 0.0);
        assertEquals(cf.evaluate(1.0), 1.0, 0.0);
        assertEquals(cf.evaluate(2.0), 2.0, 0.0);
        assertEquals(cf.evaluate(3.0), 3.0, 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n + x;
            }
        };

        assertEquals(cf.evaluate(0.0, 0.1), 0.0, 0.1);
        assertEquals(cf.evaluate(1.0, 0.1), 1.0, 0.1);
        assertEquals(cf.evaluate(2.0, 0.1), 2.0, 0.1);
        assertEquals(cf.evaluate(3.0, 0.1), 3.0, 0.1);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n + x;
            }
        };

        assertEquals(cf.evaluate(0.0, 10), 0.0, 0.0);
        assertEquals(cf.evaluate(1.0, 10), 1.0, 0.0);
        assertEquals(cf.evaluate(2.0, 10), 2.0, 0.0);
        assertEquals(cf.evaluate(3.0, 10), 3.0, 0.0);
    }

    @Test(expected=ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected=MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n + x;
            }
        };

        cf.evaluate(1.0, 1);
    }

    @Test(expected=ConvergenceException.class)
    public void testEvaluateInfinityDivergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.POSITIVE_INFINITY;
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        cf.evaluate(1.0);
    }

    @Test(expected=ConvergenceException.class)
    public void testEvaluateNanDivergenceException() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return Double.NaN;
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        cf.evaluate(1.0);
    }
}