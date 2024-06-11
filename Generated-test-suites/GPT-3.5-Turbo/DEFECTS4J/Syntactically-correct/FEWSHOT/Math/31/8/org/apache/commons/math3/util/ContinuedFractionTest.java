
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContinuedFractionTest {

    private static final double EPSILON = 1e-9;

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

        assertEquals(2.0, cf.getA(1, 1.0), EPSILON);
        assertEquals(3.5, cf.getA(2, 1.5), EPSILON);
        assertEquals(0.0, cf.getA(0, 0.0), EPSILON);
        assertEquals(10.0, cf.getA(5, 5.0), EPSILON);
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

        assertEquals(0.0, cf.getB(1, 1.0), EPSILON);
        assertEquals(-0.5, cf.getB(2, 1.5), EPSILON);
        assertEquals(0.0, cf.getB(0, 0.0), EPSILON);
        assertEquals(0.0, cf.getB(5, 5.0), EPSILON);
    }

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

        assertEquals(9.0 / 4.0, cf.evaluate(1.0), EPSILON);
        assertEquals(389.0 / 192.0, cf.evaluate(1.5), EPSILON);
        assertEquals(1.0, cf.evaluate(0.0), EPSILON);
        assertEquals(975.0 / 144.0, cf.evaluate(5.0), EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilon() {
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

        assertEquals(9.0 / 4.0, cf.evaluate(1.0, EPSILON), EPSILON);
        assertEquals(389.0 / 192.0, cf.evaluate(1.5, EPSILON), EPSILON);
        assertEquals(1.0, cf.evaluate(0.0, EPSILON), EPSILON);
        assertEquals(975.0 / 144.0, cf.evaluate(5.0, EPSILON), EPSILON);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
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

        assertEquals(9.0 / 4.0, cf.evaluate(1.0, 100), EPSILON);
        assertEquals(389.0 / 192.0, cf.evaluate(1.5, 200), EPSILON);
        assertEquals(1.0, cf.evaluate(0.0, 300), EPSILON);
        assertEquals(975.0 / 144.0, cf.evaluate(5.0, 400), EPSILON);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
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

        assertEquals(9.0 / 4.0, cf.evaluate(1.0, EPSILON, 100), EPSILON);
        assertEquals(389.0 / 192.0, cf.evaluate(1.5, EPSILON, 200), EPSILON);
        assertEquals(1.0, cf.evaluate(0.0, EPSILON, 300), EPSILON);
        assertEquals(975.0 / 144.0, cf.evaluate(5.0, EPSILON, 400), EPSILON);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
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

        cf.evaluate(0.0, 0.0, 100);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
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

        cf.evaluate(1.0, EPSILON, 1);
    }

}
