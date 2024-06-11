package com.google.common.math;

import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2L)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8L)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10L)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15L)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1L), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2L), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8L), RoundingMode.UNNECESSARY));
        assertEquals(9, BigIntegerMath.log2(BigInteger.valueOf(512L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1L), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10L), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100L), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(1L), BigIntegerMath.sqrt(BigInteger.valueOf(1L), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2L), BigIntegerMath.sqrt(BigInteger.valueOf(4L), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3L), BigIntegerMath.sqrt(BigInteger.valueOf(9L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(1L), BigIntegerMath.divide(BigInteger.valueOf(2L), BigInteger.valueOf(2L), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2L), BigIntegerMath.divide(BigInteger.valueOf(4L), BigInteger.valueOf(2L), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(5L), BigIntegerMath.divide(BigInteger.valueOf(15L), BigInteger.valueOf(3L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1L), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1L), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(120L), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1L), BigIntegerMath.binomial(4, 0));
        assertEquals(BigInteger.valueOf(6L), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10L), BigIntegerMath.binomial(5, 2));
    }
}