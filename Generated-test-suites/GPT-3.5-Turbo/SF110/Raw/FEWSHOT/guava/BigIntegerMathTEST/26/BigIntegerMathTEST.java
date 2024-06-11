
package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.TWO));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(64)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(65)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(100)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.UNNECESSARY));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(63), RoundingMode.UNNECESSARY));
        assertEquals(7, BigIntegerMath.log2(BigInteger.valueOf(100), RoundingMode.CEILING));
        assertEquals(7, BigIntegerMath.log2(BigInteger.TEN.pow(7).subtract(BigInteger.ONE), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(99), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.TEN.pow(3).subtract(BigInteger.ONE), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(9), BigIntegerMath.sqrt(BigInteger.valueOf(81), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.valueOf(2), RoundingMode.HALF_UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("30414093201713378043612608166064768844377641568960512000000000000"), BigIntegerMath.factorial(50));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("2629575"), BigIntegerMath.binomial(25, 10));
        assertEquals(new BigInteger("1124000727777607680000"), BigIntegerMath.binomial(40, 20));
    }

    @Test
    public void testFitsInLong() {
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));
    }
}

