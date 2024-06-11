package com.google.common.math;

import org.junit.*;
import static org.junit.Assert.*;
import java.math.*;

public class BigIntegerMathTest {

    // Test for isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(0)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-5)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
    }

    // Test for log2 method
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(63, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UNNECESSARY));
    }

    // Test for log10 method
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(99), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
    }

    // Test for sqrt method
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(0), BigIntegerMath.sqrt(BigInteger.valueOf(0), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(11), BigIntegerMath.sqrt(BigInteger.valueOf(120), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(319), BigIntegerMath.sqrt(BigInteger.valueOf(101761), RoundingMode.UNNECESSARY));
    }

    // Test for divide method
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(8), BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(9), BigIntegerMath.divide(BigInteger.valueOf(81), BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
    }

    // Test for factorial method
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
    }

    // Test for binomial method
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 5));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 3));
    }
}