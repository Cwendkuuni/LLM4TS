package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
    }

    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("6"), BigIntegerMath.divide(new BigInteger("12"), new BigInteger("2"), RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
}