package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-1")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("6"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("9"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("9"), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("11"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("99"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("101"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("9999"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("10001"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("0"), BigIntegerMath.sqrt(new BigInteger("0"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("5"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("8"), BigIntegerMath.sqrt(new BigInteger("64"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("9"), BigIntegerMath.sqrt(new BigInteger("81"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("-2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("-2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("-2"), BigIntegerMath.divide(new BigInteger("-4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("-4"), new BigInteger("-2"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0,0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1,0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1,1));
        assertEquals(new BigInteger("3"), BigIntegerMath.binomial(3,1));
        assertEquals(new BigInteger("15"), BigIntegerMath.binomial(6,2));
        assertEquals(new BigInteger("126"), BigIntegerMath.binomial(9,4));
    }
}