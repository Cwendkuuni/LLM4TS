package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double result = cf.evaluate(2.0);
        assertEquals(2.6885245901639346, result, 1e-15);
    }

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double a = cf.getA(3, 2.0);
        assertEquals(5.0, a, 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n + x;
            }

            @Override
            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double b = cf.getB(3, 2.0);
        assertEquals(1.0, b, 0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return -1.0;
            }
        };

        cf.evaluate(2.0, 1e-15, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceError() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };

        cf.evaluate(2.0, 1e-15, Integer.MAX_VALUE);
    }

}