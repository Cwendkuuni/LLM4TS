package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TWO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.TWO, RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.FLOOR));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.TWO, BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TWO, BigInteger.TWO, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.TWO, BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(2, 1));
    }
}