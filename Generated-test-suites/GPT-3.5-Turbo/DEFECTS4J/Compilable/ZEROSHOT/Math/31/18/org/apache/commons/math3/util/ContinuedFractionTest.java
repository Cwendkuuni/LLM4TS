package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double a = continuedFraction.getA(3, 2.5);
        assertEquals(5.5, a, 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double b = continuedFraction.getB(3, 2.5);
        assertEquals(-0.5, b, 0.0);
    }

    @Test
    public void testEvaluateDefaultEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double value = continuedFraction.evaluate(2.5);
        assertEquals(0.625, value, 0.01);
    }

    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double value = continuedFraction.evaluate(2.5, 0.001);
        assertEquals(0.625, value, 0.001);
    }

    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double value = continuedFraction.evaluate(2.5, 0.001, 10);
        assertEquals(-1.25, value, 0.01);
    }

    @Test
    public void testEvaluateAllParameters() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n + x;
            }

            protected double getB(int n, double x) {
                return n - x;
            }
        };

        double value = continuedFraction.evaluate(2.5, 0.001, 10);
        assertEquals(-1.25, value, 0.01);
    }
}