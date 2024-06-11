package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class ContinuedFractionTest {
    /**
     * Test for evaluate method with default epsilon and maxIterations.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        assertEquals(1.618051232063466, continuedFraction.evaluate(0.0), 1e-9);
    }

    /**
     * Test for evaluate method with custom epsilon.
     */
    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        assertEquals(1.618051232063466, continuedFraction.evaluate(0.0, 1e-10), 1e-11);
    }

    /**
     * Test for evaluate method with custom maxIterations.
     */
    @Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        assertEquals(1.618051232063466, continuedFraction.evaluate(0.0, 10), 1e-9);
    }

    /**
     * Test for evaluate method with convergence exception.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, 1e-255, 1);
    }

    /**
     * Test for evaluate method with maxCountException.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountException() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, 1e-9, 1);
    }

    /**
     * Test for getA method.
     */
    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        assertArrayEquals(new double[] {1.0, 2.0, 1.0, 3.0, 1.0, 5.0, 1.0, 8.0, 1.0},
                MathArrays.create(continuedFraction.getA(0, 0.0)), 1e-9);
    }

    /**
     * Test for getB method.
     */
    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        assertArrayEquals(new double[] {0.0, 2.0, 1.0, 2.0, 1.0, 3.0, 1.0, 5.0, 2.0},
                MathArrays.create(continuedFraction.getB(0, 0.0)), 1e-9);
    }

    /**
     * Test for evaluate method with custom a and b coefficients.
     */
    @Test
    public void testCustomAAndB() {
        ContinuedFraction continuedFraction = new CustomAAndBContinuedFraction();
        assertEquals(1.4142135623730955, continuedFraction.evaluate(0.0), 1e-9);
    }
}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
            case 0:
            case 1:
                return 1.0;
            case 2:
                return 2.0;
            default:
                return 1.0;
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
            case 0:
            case 1:
                return 0.0;
            default:
                return 1.0;
        }
    }
}

class CustomAAndBContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
            case 0:
                return 1.0;
            case 1:
                return 2.0;
            case 2:
                return 1.0;
            case 3:
                return 3.0;
            case 4:
                return 1.0;
            case 5:
                return 5.0;
            case 6:
                return 1.0;
            case 7:
                return 8.0;
            case 8:
                return 1.0;
            default:
                return 0.0;
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
            case 0:
                return 0.0;
            case 1:
                return 2.0;
            case 2:
                return 1.0;
            case 3:
                return 2.0;
            case 4:
                return 1.0;
            case 5:
                return 3.0;
            case 6:
                return 1.0;
            case 7:
                return 5.0;
            case 8:
                return 2.0;
            default:
                return 0.0;
        }
    }
}