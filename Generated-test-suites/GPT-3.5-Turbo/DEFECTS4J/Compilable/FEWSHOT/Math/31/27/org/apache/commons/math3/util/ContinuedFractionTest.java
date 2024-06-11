package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0, cf.getA(0, 5), 0);
        assertEquals(5, cf.getA(1, 5), 0);
        assertEquals(10, cf.getA(2, 5), 0);
        assertEquals(15, cf.getA(3, 5), 0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0, cf.getB(0, 5), 0);
        assertEquals(1, cf.getB(1, 5), 0);
        assertEquals(2, cf.getB(2, 5), 0);
        assertEquals(3, cf.getB(3, 5), 0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.evaluate(0), 0);
        assertEquals(1.0, cf.evaluate(1), 0);
        assertEquals(1.5, cf.evaluate(2), 0);
        assertEquals(2.0, cf.evaluate(3), 0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.evaluate(0, 0.001), 0.001);
        assertEquals(1.0, cf.evaluate(1, 0.001), 0.001);
        assertEquals(1.5, cf.evaluate(2, 0.001), 0.001);
        assertEquals(2.0, cf.evaluate(3, 0.001), 0.001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.evaluate(0, Integer.MAX_VALUE), 0);
        assertEquals(1.0, cf.evaluate(1, Integer.MAX_VALUE), 0);
        assertEquals(1.5, cf.evaluate(2, Integer.MAX_VALUE), 0);
        assertEquals(2.0, cf.evaluate(3, Integer.MAX_VALUE), 0);
    }

    @Test
    public void testEvaluateWithAllParameters() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return n;
            }
        };

        assertEquals(0.0, cf.evaluate(0, 0.001, Integer.MAX_VALUE), 0.001);
        assertEquals(1.0, cf.evaluate(1, 0.001, Integer.MAX_VALUE), 0.001);
        assertEquals(1.5, cf.evaluate(2, 0.001, Integer.MAX_VALUE), 0.001);
        assertEquals(2.0, cf.evaluate(3, 0.001, Integer.MAX_VALUE), 0.001);
    }
}