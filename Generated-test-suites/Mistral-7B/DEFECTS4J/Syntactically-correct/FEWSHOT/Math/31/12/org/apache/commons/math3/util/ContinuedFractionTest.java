package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for class ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test for evaluate method with default arguments.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertEquals(cf.evaluate(0.0), 1.0, 1e-9);
    }

    /**
     * Test for evaluate method with custom arguments.
     */
    @Test
    public void testEvaluateCustom() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertEquals(cf.evaluate(0.0, 1e-12, 1000), 1.6480517079500512, 1e-13);
    }

    /**
     * Test for evaluate method with maxIterations less than 1.
     */
    @Test
    public void testEvaluateMaxIterationsOne() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> cf.evaluate(0.0, 1e-12, 0));
    }

    /**
     * Test for evaluate method with epsilon less than 0.
     */
    @Test
    public void testEvaluateEpsilonNegative() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> cf.evaluate(0.0, -1e-12, Integer.MAX_VALUE));
    }

    /**
     * Test for evaluate method with infinite epsilon.
     */
    @Test
    public void testEvaluateEpsilonInfinite() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertThrows(IllegalArgumentException.class, () -> cf.evaluate(0.0, Double.POSITIVE_INFINITY, Integer.MAX_VALUE));
    }

    /**
     * Test for evaluate method with ConvergenceException.
     */
    @Test
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new TestContinuedFractionWithDivergence();
        assertThrows(ConvergenceException.class, () -> cf.evaluate(0.0, 1e-12, Integer.MAX_VALUE));
    }

    /**
     * Test for evaluate method with MaxCountExceededException.
     */
    @Test
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new TestContinuedFractionWithMaxCountExceededException();
        assertThrows(MaxCountExceededException.class, () -> cf.evaluate(0.0, 1e-12, 1));
    }

    /**
     * Test for getA method.
     */
    @Test
    public void testGetA() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertEquals(cf.getA(0, 0.0), 1.0, 1e-9);
        assertEquals(cf.getA(1, 0.0), 1.0, 1e-9);
        assertEquals(cf.getA(2, 0.0), 2.0, 1e-9);
        assertEquals(cf.getA(3, 0.0), 2.0, 1e-9);
        assertEquals(cf.getA(4, 0.0), 1.0, 1e-9);
    }

    /**
     * Test for getB method.
     */
    @Test
    public void testGetB() {
        ContinuedFraction cf = new TestContinuedFraction();
        assertEquals(cf.getB(0, 0.0), 1.0, 1e-9);
        assertEquals(cf.getB(1, 0.0), 1.0, 1e-9);
        assertEquals(cf.getB(2, 0.0), 1.0, 1e-9);
        assertEquals(cf.getB(3, 0.0), 2.0, 1e-9);
        assertEquals(cf.getB(4, 0.0), 1.0, 1e-9);
    }

}

class TestContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
        case 0:
        case 1:
        case 4:
            return 1.0;
        case 2:
        case 3:
            return 2.0;
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
        case 0:
        case 1:
        case 4:
            return 1.0;
        case 2:
        case 3:
            return 1.0;
        default:
            throw new UnsupportedOperationException();
        }
    }
}

class TestContinuedFractionWithDivergence extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        switch (n) {
        case 0:
            return 1.0 / FastMath.sqrt(FastMath.abs(x - 1.0));
        case 1:
            return 1.0;
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    protected double getB(int n, double x) {
        switch (n) {
        case 0:
            return 1.0;
        case 1:
            return 1.0 / FastMath.sqrt(FastMath.abs(x - 1.0));
        default:
            throw new UnsupportedOperationException();
        }
    }
}

class TestContinuedFractionWithMaxCountExceededException extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n == 1 ? 1.0 : -1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return n == 1 ? 1.0 : 1.0;
    }
}