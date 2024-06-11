package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    private ContinuedFraction continuedFraction;

    @Before
    public void setup() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? x : n == 1 ? 1 : n % 2 == 0 ? x : 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : n == 1 ? x : x - 1;
            }
        };
    }

    @Test
    public void testGetA() {
        for (int i = 0; i < 10; i++) {
            assertEquals(i % 2 == 0 ? i : 1, continuedFraction.getA(i, 0.0), 1e-8);
        }
    }

    @Test
    public void testGetB() {
        for (int i = 0; i < 10; i++) {
            if (i == 0 || i == 1) {
                assertEquals(i % 2 == 0 ? 1 : 0.0, continuedFraction.getB(i, 0.0), 1e-8);
            } else {
                assertEquals(i % 2 == 0 ? i - 1 : 1.0, continuedFraction.getB(i, 0.0), 1e-8);
            }
        }
    }

    @Test
    public void testEvaluateWithDefaultArguments() {
        double result = continuedFraction.evaluate(0.0);
        assertEquals(1.0, result, 1e-8);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        double result = continuedFraction.evaluate(0.0, 1e-10);
        assertEquals(1.0, result, 1e-11);
    }

    @Test
    public void testEvaluateWithCustomMaxIterations() {
        double result = continuedFraction.evaluate(0.0, 10);
        assertEquals(1.0, result, 1e-8);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateDivergenceException() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? x : Double.POSITIVE_INFINITY;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        continuedFraction.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        continuedFraction.evaluate(0.0, 0);
    }

}