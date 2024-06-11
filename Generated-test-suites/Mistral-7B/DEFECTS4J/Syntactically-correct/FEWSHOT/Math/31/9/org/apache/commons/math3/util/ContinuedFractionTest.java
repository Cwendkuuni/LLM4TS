package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import java.util.Arrays;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        ContinuedFraction cf = new ContinuedFraction() {
            // Override the abstract methods here to check for UnsupportedOperationException
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> cf.evaluate(1.0));
    }

    @Test
    public void testEvaluate() {
        ContinuedFractionSqrt sqrt = new ContinuedFractionSqrt();
        assertEquals(1.0, sqrt.evaluate(1.0), 1e-10);
        assertEquals(2.0, sqrt.evaluate(4.0), 1e-10);
        assertEquals(Math.sqrt(2.0), sqrt.evaluate(4.0, 1e-5, 10), 1e-5);
        assertEquals(Math.sqrt(2.0), sqrt.evaluate(4.0, 1e-15, 100), 1e-15);

        // Test with the default epsilon and maxIterations values
        assertEquals(Math.E, new ContinuedFractionEulerMascheroni().evaluate(1.0), 1e-10);

        // Test convergence exception
        assertThrows(ConvergenceException.class, () -> new ContinuedFractionLog2().evaluate(-1.0, 1e-10, 10));

        // Test maxCountExceeded exception
        assertThrows(MaxCountExceededException.class, () -> new ContinuedFractionLog2().evaluate(1.0, 1e-30, 1));
    }

    @Test
    public void testEvaluateWithCustomEpsilonAndMaxIterations() {
        ContinuedFractionLog2 log2 = new ContinuedFractionLog2();
        assertEquals(Math.log(2.0), log2.evaluate(1.0, 1e-10, 100), 1e-10);
    }

    @Test
    public void testEvaluateWithNegativeX() {
        ContinuedFractionLog2 log2 = new ContinuedFractionLog2();
        // Test log base 2 of a negative number
        assertEquals(-1.0 * Math.log(2.0), log2.evaluate(-1.0, 1e-10, 100), 1e-10);
    }

    @Test
    public void testEvaluateWithZeroX() {
        ContinuedFractionSqrt sqrt = new ContinuedFractionSqrt();
        assertEquals(0.0, sqrt.evaluate(0.0), 1e-10);

        ContinuedFractionLog2 log2 = new ContinuedFractionLog2();
        assertEquals(Double.NEGATIVE_INFINITY, log2.evaluate(0.0, 1e-10, 100), 1e-10);
    }

    @Test
    public void testEvaluateWithInfiniteX() {
        ContinuedFractionLog2 log2 = new ContinuedFractionLog2();
        assertEquals(Double.POSITIVE_INFINITY, log2.evaluate(Double.POSITIVE_INFINITY, 1e-10, 100), 1e-10);
    }

    @Test
    public void testEvaluateLargeX() {
        ContinuedFractionLog2 log2 = new ContinuedFractionLog2();
        assertEquals(Math.log(1024.0), log2.evaluate(1024.0, 1e-10, 100), 1e-10);
    }

    @Test
    public void testBigDecimalEvaluate() {
        ContinuedFractionSqrt sqrt = new ContinuedFractionSqrt();

        BigDecimal x = new BigDecimal("2.0");
        BigDecimal expected = new BigDecimal("1.4142135625");
        assertEquals(expected, sqrt.evaluate(x.doubleValue(), 10));

        BigDecimal x2 = new BigDecimal("4.0");
        expected = new BigDecimal("2.0");
        assertEquals(expected, sqrt.evaluate(x2.doubleValue(), 10));

        BigDecimal x3 = new BigDecimal("16.0");
        expected = new BigDecimal("4.0");
        assertEquals(expected, sqrt.evaluate(x3.doubleValue(), 10));
    }

    @Test
    public void testStringEvaluate() {
        ContinuedFractionSqrt sqrt = new ContinuedFractionSqrt();

        assertEquals(1.0, sqrt.evaluate("1.0"), 1e-10);
        assertEquals(2.0, sqrt.evaluate("4.0"), 1e-10);
        assertEquals(Math.sqrt(2.0), sqrt.evaluate("4.0", "1e-5"), 1e-5);
        assertEquals(Math.sqrt(2.0), sqrt.evaluate("4.0", "1e-15"), 1e-15);
    }

    @Test
    public void testBigDecimalStringEvaluate() {
        ContinuedFractionSqrt sqrt = new ContinuedFractionSqrt();

        BigDecimal x = new BigDecimal("2.0");
        BigDecimal expected = new BigDecimal("1.4142135625");
        assertEquals(expected, sqrt.evaluate("1.0", x.toString()));

        BigDecimal x2 = new BigDecimal("4.0");
        expected = new BigDecimal("2.0");
        assertEquals(expected, sqrt.evaluate("1.0", x2.toString()));

        BigDecimal x3 = new BigDecimal("16.0");
        expected = new BigDecimal("4.0");
        assertEquals(expected, sqrt.evaluate("1.0", x3.toString()));
    }
}

public class ContinuedFractionSqrt extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n == 0 ? 2.0 : 2.0 * x;
    }

    @Override
    protected double getB(int n, double x) {
        return n == 0 ? 1.0 : (n - 1.0) * x;
    }
}

public class ContinuedFractionLog2 extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return n == 0 ? 1.0 : (n - 1.0) / x;
    }

    @Override
    protected double getB(int n, double x) {
        return n == 0 ? x : 1.0;
    }
}