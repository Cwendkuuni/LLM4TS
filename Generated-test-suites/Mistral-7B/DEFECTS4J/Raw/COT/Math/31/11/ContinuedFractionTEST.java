package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for ContinuedFraction.
 */
public class ContinuedFractionTest {

    /**
     * Test evaluate method with default arguments.
     */
    @org.junit.Test
    public void testEvaluateDefaultArguments() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0);
        double expectedResult = 1.4142135623730955;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test evaluate method with custom epsilon.
     */
    @org.junit.Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0, 1e-12);
        double expectedResult = 1.4142137311525732;
        assertEquals(expectedResult, result, 1e-12);
    }

    /**
     * Test evaluate method with custom maxIterations.
     */
    @org.junit.Test
    public void testEvaluateCustomMaxIterations() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.evaluate(2.0, 10);
        double expectedResult = 1.4142136622070625;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test evaluate method with infinite value.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateInfiniteValue() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(Double.POSITIVE_INFINITY);
    }

    /**
     * Test evaluate method with NaN value.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testEvaluateNaNValue() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(Double.NaN);
    }

    /**
     * Test evaluate method with negative value.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testEvaluateNegativeValue() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.evaluate(-1.0);
    }

    /**
     * Test getA method with valid arguments.
     */
    @org.junit.Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.getA(0, 2.0);
        double expectedResult = 2.0;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test getA method with invalid arguments.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetAInvalidArguments() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.getA(-1, 2.0);
    }

    /**
     * Test getB method with valid arguments.
     */
    @org.junit.Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        double result = continuedFraction.getB(0, 2.0);
        double expectedResult = 1.0;
        assertEquals(expectedResult, result, 1e-9);
    }

    /**
     * Test getB method with invalid arguments.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testGetBInvalidArguments() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        continuedFraction.getB(-1, 2.0);
    }

    /**
     * Test evaluate method with BigDecimal and BigInteger values.
     */
    @org.junit.Test
    public void testEvaluateBigDecimalAndBigInteger() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        BigDecimal x = new BigDecimal("2.0");
        BigInteger maxIterations = BigInteger.valueOf(10);
        BigDecimal result = continuedFraction.evaluate(x, new BigDecimal("1e-9"), maxIterations);
        BigDecimal expectedResult = new BigDecimal("1.4142136622070625");
        assertEquals(expectedResult, result);
    }

    /**
     * Test evaluate method with custom epsilon and BigDecimal value.
     */
    @org.junit.Test
    public void testEvaluateCustomEpsilonBigDecimal() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        BigDecimal x = new BigDecimal("2.0");
        BigDecimal epsilon = new BigDecimal("1e-12");
        BigDecimal result = continuedFraction.evaluate(x, epsilon);
        BigDecimal expectedResult = new BigDecimal("1.4142137311525732");
        assertEquals(expectedResult, result);
    }

    /**
     * Test evaluate method with custom maxIterations and BigDecimal value.
     */
    @org.junit.Test
    public void testEvaluateCustomMaxIterationsBigDecimal() {
        ContinuedFraction continuedFraction = new ContinuedFractionSqrt2();
        BigDecimal x = new BigDecimal("2.0");
        BigInteger maxIterations = BigInteger.valueOf(10);
        BigDecimal result = continuedFraction.evaluate(x, new BigDecimal("1e-9"), maxIterations);
        BigDecimal expectedResult = new BigDecimal("1.4142136622070625");
        assertEquals(expectedResult, result);
    }
}

class ContinuedFractionSqrt2 extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 1.0 / (2.0 * x + 2.0 * FastMath.sqrt(x));
        } else {
            return 2.0 * x + 2.0 * FastMath.sqrt(x);
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        } else {
            return 2.0 * x + 2.0 * FastMath.sqrt(x);
        }
    }
}