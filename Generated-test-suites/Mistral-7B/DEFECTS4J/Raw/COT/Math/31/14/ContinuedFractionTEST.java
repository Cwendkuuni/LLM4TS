package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ContinuedFractionTest {

    private ContinuedFraction cf = new MyContinuedFraction();

    @org.junit.Test
    public void testEvaluateDefault() {
        double result = cf.evaluate(1.0);
        double expected = 3.0;
        assertEquals(expected, result, 1e-8);
    }

    @org.junit.Test
    public void testEvaluateEpsilon() {
        double result = cf.evaluate(1.0, 1e-12);;
        double expected = 3.0;
        assertEquals(expected, result, 1e-13);
    }

    @org.junit.Test
    public void testEvaluateMaxIterations() {
        assertThrows(MaxCountExceededException.class, () -> {
            cf.evaluate(1.0, 1, 1);
        });
    }

    @org.junit.Test
    public void testEvaluateInfinite() {
        assertThrows(ConvergenceException.class, () -> {
            cf.evaluate(Double.POSITIVE_INFINITY, 1e-9, Integer.MAX_VALUE);
        });

        assertThrows(ConvergenceException.class, () -> {
            cf.evaluate(Double.NEGATIVE_INFINITY, 1e-9, Integer.MAX_VALUE);
        });
    }

    @org.junit.Test
    public void testEvaluateNaN() {
        assertThrows(ConvergenceException.class, () -> {
            cf.evaluate(Double.NaN, 1e-9, Integer.MAX_VALUE);
        });
    }

    @org.junit.Test
    public void testGetAWithZeroArgument() {
        double result = cf.getA(0, 0.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-6);
    }

    @org.junit.Test
    public void testGetAWithNegativeArgument() {
        double result = cf.getA(-1, 0.0);
        assertThrows(IllegalArgumentException.class, () -> cf.getA(-1, 0.0));
    }

    @org.junit.Test
    public void testGetAWithNonIntegerArgument() {
        double result = cf.getA(1.5, 0.0);
        assertThrows(IllegalArgumentException.class, () -> cf.getA(1.5, 0.0));
    }

    @org.junit.Test
    public void testGetBWithZeroArgument() {
        double result = cf.getB(0, 0.0);
        double expected = 2.0;
        assertEquals(expected, result, 1e-6);
    }

    @org.junit.Test
    public void testGetBWithNegativeArgument() {
        double result = cf.getB(-1, 0.0);
        assertThrows(IllegalArgumentException.class, () -> cf.getB(-1, 0.0));
    }

    @org.junit.Test
    public void testGetBWithNonIntegerArgument() {
        double result = cf.getB(1.5, 0.0);
        assertThrows(IllegalArgumentException.class, () -> cf.getB(1.5, 0.0));
    }

    @org.junit.Test
    public void testEvaluateSquareRootOfTwo() {
        cf = new SquareRootOfTwoContinuedFraction();
        double result = cf.evaluate(2.0);
        double expected = 1.5;
        assertEquals(expected, result, 1e-8);
    }

    @org.junit.Test
    public void testEvaluateEulerMascheroniConstant() {
        cf = new EulerMascheroniConstantContinuedFraction();
        double result = cf.evaluate(0.0);
        double expected = 0.57721566490153286060651209008240243104265;
        assertEquals(new BigDecimal(expected).toPlainString(), new BigDecimal(result).toPlainString(), 500);
    }
}

class MyContinuedFraction extends ContinuedFraction {
    // Empty implementation
}

class SquareRootOfTwoContinuedFraction extends MyContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n % 2 == 0 ? 1.0 : 2.0 * x;
    }

    @Override
    protected double getB(int n, double x) {
        return n % 2 == 0 ? x + 1.0 : -1.0;
    }
}

class EulerMascheroniConstantContinuedFraction extends MyContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n == 0) {
            return 1.0;
        } else if (n % 2 == 0) {
            return -1.0 / (2.0 * n + 1.0);
        } else {
            return 1.0 / (2.0 * n + 1.0);
        }
    }

    @Override
    protected double getB(int n, double x) {
        return 1.0 / (1.0 + 1.0 / (2.0 * n + 1.0));
    }
}