package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import org.junit.Test;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test method for {@link ContinuedFraction#getA(int, double)}.
     */
    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        for (int i = 0; i < 10; i++) {
            assertEquals("Unexpected result for getA(i, 0.0)", continuedFraction.getA(i, 0.0), i + 1.0, 1e-6);
        }
    }

    /**
     * Test method for {@link ContinuedFraction#getB(int, double)}.
     */
    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        for (int i = 0; i < 10; i++) {
            assertEquals("Unexpected result for getB(i, 0.0)", continuedFraction.getB(i, 0.0), i + 2.0, 1e-6);
        }
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double)}.
     */
    @Test
    public void testEvaluate_Double() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double value = continuedFraction.evaluate(0.0);
        assertEquals("Unexpected result for evaluate(0.0)", 1.0 / Math.sqrt(2), value, 1e-6);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double)}.
     */
    @Test
    public void testEvaluate_Double_Double() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double value = continuedFraction.evaluate(0.0, 1e-10);
        assertEquals("Unexpected result for evaluate(0.0, epsilon)", 1.0 / Math.sqrt(2), value, 1e-11);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, int)}.
     */
    @Test
    public void testEvaluate_Double_Int() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double value = continuedFraction.evaluate(0.0, 100);
        assertEquals("Unexpected result for evaluate(0.0, maxIterations)", 1.0 / Math.sqrt(2), value, 1e-11);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)}.
     */
    @Test
    public void testEvaluate_Double_Double_Int() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        double value = continuedFraction.evaluate(0.0, 1e-10, 100);
        assertEquals("Unexpected result for evaluate(0.0, epsilon, maxIterations)", 1.0 / Math.sqrt(2), value, 1e-12);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with infinite result.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluate_Infinite() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(1.0 / 0.0, 1e-10, 100);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with NaN result.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluate_NaN() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(Double.NaN, 1e-10, 100);
    }

    /**
     * Test method for {@link ContinuedFraction#evaluate(double, double, int)} with maxIterations exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_MaxCountExceeded() {
        ContinuedFraction continuedFraction = new TestContinuedFraction();
        continuedFraction.evaluate(0.0, 1e-10, 1);
    }

}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n + 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return n + 2.0;
    }
}