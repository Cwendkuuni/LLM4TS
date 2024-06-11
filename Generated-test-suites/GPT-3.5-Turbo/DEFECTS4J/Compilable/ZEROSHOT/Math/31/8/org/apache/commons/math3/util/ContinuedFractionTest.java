package org.apache.commons.math3.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1;
                } else if (n == 1) {
                    return 2;
                } else {
                    return 3;
                }
            }

            protected double getB(int n, double x) {
                return 0;
            }
        };

        assertEquals(1.0, cf.getA(0, 0.0), EPSILON);
        assertEquals(2.0, cf.getA(1, 0.0), EPSILON);
        assertEquals(3.0, cf.getA(2, 0.0), EPSILON);
    }

    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                return 0;
            }

            protected double getB(int n, double x) {
                if (n == 0) {
                    return 1;
                } else if (n == 1) {
                    return 2;
                } else {
                    return 3;
                }
            }
        };

        assertEquals(1.0, cf.getB(0, 0.0), EPSILON);
        assertEquals(2.0, cf.getB(1, 0.0), EPSILON);
        assertEquals(3.0, cf.getB(2, 0.0), EPSILON);
    }

    @Test
    public void testEvaluate() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1;
                } else if (n == 1) {
                    return x + 1;
                } else {
                    return n;
                }
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0), EPSILON);
        assertEquals(1.5, cf.evaluate(1.0), EPSILON);
        assertEquals(1.6666666666666667, cf.evaluate(2.0), EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1;
                } else {
                    return n + 1;
                }
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, EPSILON), EPSILON);
        assertEquals(1.5, cf.evaluate(1.0, EPSILON), EPSILON);
        assertEquals(1.6666666666666667, cf.evaluate(2.0, EPSILON), EPSILON);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1;
                } else {
                    return n + 1;
                }
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, Integer.MAX_VALUE), EPSILON);
        assertEquals(1.5, cf.evaluate(1.0, Integer.MAX_VALUE), EPSILON);
        assertEquals(1.6666666666666667, cf.evaluate(2.0, Integer.MAX_VALUE), EPSILON);
    }

    @Test
    public void testEvaluateWithCustomEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            protected double getA(int n, double x) {
                if (n == 0) {
                    return 1;
                } else {
                    return n + 1;
                }
            }

            protected double getB(int n, double x) {
                return 1;
            }
        };

        assertEquals(1.0, cf.evaluate(0.0, 1e-6, Integer.MAX_VALUE), EPSILON);
        assertEquals(1.5, cf.evaluate(1.0, 1e-6, Integer.MAX_VALUE), EPSILON);
        assertEquals(1.6666666666666667, cf.evaluate(2.0, 1e-6, Integer.MAX_VALUE), EPSILON);
    }

}