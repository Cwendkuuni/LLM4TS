package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ContinuedFractionTest {
    @Test
    public void testGetA() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n * x;
            }

            protected double getB(int n, double x) {
                return 1;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(0, cf.getA(0, 1.0), 0);
        assertEquals(2, cf.getA(1, 1.0), 0);
        assertEquals(4, cf.getA(2, 1.0), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 3 * n;
            }

            protected double getB(int n, double x) {
                return 2;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(0, cf2.getA(0, 2.0), 0);
        assertEquals(3, cf2.getA(1, 2.0), 0);
        assertEquals(6, cf2.getA(2, 2.0), 0);
    }

    @Test
    public void testGetB() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n * x;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(0, cf.getB(0, 1.0), 0);
        assertEquals(1, cf.getB(1, 1.0), 0);
        assertEquals(2, cf.getB(2, 1.0), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 2 * n;
            }

            protected double getB(int n, double x) {
                return 3;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(0, cf2.getB(0, 2.0), 0);
        assertEquals(3, cf2.getB(1, 2.0), 0);
        assertEquals(3, cf2.getB(2, 2.0), 0);
    }

    @Test
    public void testEvaluate() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 1;
            }

            protected double getB(int n, double x) {
                return 2;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(1.0, cf.evaluate(1.0), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n * 2;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(1.0, cf2.evaluate(1.0), 0);
        assertEquals(0.5, cf2.evaluate(0.5), 0);
        assertEquals(2.0, cf2.evaluate(2.0), 0);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 1;
            }

            protected double getB(int n, double x) {
                return 2;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(1.0, cf.evaluate(1.0, 0.01), 0);
        assertEquals(1.0, cf.evaluate(1.0, 0.001), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n * 2;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(1.0, cf2.evaluate(1.0, 0.1), 0);
        assertEquals(0.5, cf2.evaluate(0.5, 0.01), 0);
        assertEquals(2.0, cf2.evaluate(2.0, 0.001), 0);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 1;
            }

            protected double getB(int n, double x) {
                return 2;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(1.0, cf.evaluate(1.0, 10), 0);
        assertEquals(1.0, cf.evaluate(1.0, 20), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n * 2;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(1.0, cf2.evaluate(1.0, 10), 0);
        assertEquals(0.5, cf2.evaluate(0.5, 20), 0);
        assertEquals(2.0, cf2.evaluate(2.0, 30), 0);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        // Test case 1
        class TestContinuedFraction extends ContinuedFraction {
            protected double getA(int n, double x) {
                return 1;
            }

            protected double getB(int n, double x) {
                return 2;
            }
        }

        TestContinuedFraction cf = new TestContinuedFraction();
        assertEquals(1.0, cf.evaluate(1.0, 0.01, 10), 0);
        assertEquals(1.0, cf.evaluate(1.0, 0.001, 20), 0);

        // Test case 2
        class TestContinuedFraction2 extends ContinuedFraction {
            protected double getA(int n, double x) {
                return n;
            }

            protected double getB(int n, double x) {
                return n * 2;
            }
        }

        TestContinuedFraction2 cf2 = new TestContinuedFraction2();
        assertEquals(1.0, cf2.evaluate(1.0, 0.1, 10), 0);
        assertEquals(0.5, cf2.evaluate(0.5, 0.01, 20), 0);
        assertEquals(2.0, cf2.evaluate(2.0, 0.001, 30), 0);
    }
}