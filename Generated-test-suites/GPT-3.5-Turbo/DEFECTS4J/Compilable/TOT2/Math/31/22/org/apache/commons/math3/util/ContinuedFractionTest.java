package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // create a dummy subclass of ContinuedFraction for testing purposes
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                // for testing return squared n as a
                return n * n;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // test getA method
        assertEquals(1.0, cf.getA(1, 0.0), 0.0001);
        assertEquals(4.0, cf.getA(2, 0.0), 0.0001);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                // for testing return squared n as b
                return n * n;
            }
        };

        // test getB method
        assertEquals(1.0, cf.getB(1, 0.0), 0.0001);
        assertEquals(4.0, cf.getB(2, 0.0), 0.0001);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // test evaluate method
        assertEquals(1.0, cf.evaluate(0.0), 0.0001);
        assertEquals(0.25, cf.evaluate(4.0), 0.0001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // test evaluate with epsilon method
        assertEquals(1.0, cf.evaluate(0.0, 0.0001), 0.0001);
        assertEquals(0.25, cf.evaluate(4.0, 0.0001), 0.0001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 1.0;
            }

            protected double getB(int n, double x) {
                return 1.0;
            }
        };

        // test evaluate with max iterations method
        assertEquals(1.0, cf.evaluate(0.0, 100), 0.0001);
        assertEquals(0.25, cf.evaluate(4.0, 100), 0.0001);
    }
}