package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(1.0, cf.getA(0, 0.0), 0.0);
        assertEquals(0.5, cf.getA(1, 0.0), 0.0);
        assertEquals(0.3333333333333333, cf.getA(2, 0.0), 0.0);
        assertEquals(0.25, cf.getA(3, 0.0), 0.0);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(0.5, cf.getB(0, 0.0), 0.0);
        assertEquals(0.3333333333333333, cf.getB(1, 0.0), 0.0);
        assertEquals(0.25, cf.getB(2, 0.0), 0.0);
        assertEquals(0.2, cf.getB(3, 0.0), 0.0);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(1.0, cf.evaluate(0.0), 0.0);
        assertEquals(1.5, cf.evaluate(1.0), 0.0);
        assertEquals(1.8333333333333333, cf.evaluate(2.0), 0.0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, 0.01), 0.01);
        assertEquals(1.5, cf.evaluate(1.0, 0.01), 0.01);
        assertEquals(1.8333333333333333, cf.evaluate(2.0, 0.01), 0.01);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, 100), 0.0);
        assertEquals(1.5, cf.evaluate(1.0, 100), 0.0);
        assertEquals(1.8333333333333333, cf.evaluate(2.0, 100), 0.0);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0 / (n+1);
            }
            protected double getB(int n, double x) {
                return 1.0 / (n+2);
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, 0.01, 100), 0.01);
        assertEquals(1.5, cf.evaluate(1.0, 0.01, 100), 0.01);
        assertEquals(1.8333333333333333, cf.evaluate(2.0, 0.01, 100), 0.01);
    }

}