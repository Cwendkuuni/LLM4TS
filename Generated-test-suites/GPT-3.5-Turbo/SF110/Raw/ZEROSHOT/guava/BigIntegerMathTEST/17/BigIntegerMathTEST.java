package com.google.common.math;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-5)));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1024)));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16384)));
    }

    @Test
    public void testLog2() {
        Assert.assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(3), RoundingMode.DOWN));
        Assert.assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        Assert.assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.DOWN));
        Assert.assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        Assert.assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.CEILING));
        Assert.assertEquals(63, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        Assert.assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.DOWN));
        Assert.assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        Assert.assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(150), RoundingMode.DOWN));
        Assert.assertEquals(18, BigIntegerMath.log10(BigInteger.TEN.pow(18), RoundingMode.UNNECESSARY));
        Assert.assertEquals(18, BigIntegerMath.log10(BigInteger.TEN.pow(18).subtract(BigInteger.ONE), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        Assert.assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.FLOOR));
        Assert.assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(BigInteger.valueOf(149), RoundingMode.DOWN));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(BigInteger.valueOf(150), RoundingMode.FLOOR));
        Assert.assertEquals(BigInteger.valueOf(100000), BigIntegerMath.sqrt(BigInteger.valueOf(9999999999L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.TEN, RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.valueOf(3), RoundingMode.FLOOR));
        Assert.assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.valueOf(2), RoundingMode.CEILING));
        Assert.assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(200), BigInteger.valueOf(70), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        Assert.assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        Assert.assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        Assert.assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        Assert.assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        Assert.assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
        Assert.assertEquals(new BigInteger("6227020800"), BigIntegerMath.factorial(13));
    }

    @Test
    public void testBinomial() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 5));
        Assert.assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(6, 2));
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(6, 4));
        Assert.assertEquals(BigInteger.valueOf(15), BigIntegerMath.binomial(6, 3));
        Assert.assertEquals(BigInteger.valueOf(100947), BigIntegerMath.binomial(100, 3));
        Assert.assertEquals(new BigInteger("330679601996340"), BigIntegerMath.binomial(50, 25));
    }
}