package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    }

    @Test
    public void testLog2() {
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
    }
}