package com.google.common.math;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("1234567890")));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1048576")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.UNNECESSARY));
        assertEquals(30, BigIntegerMath.log2(new BigInteger("1073741824"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("6"), RoundingMode.DOWN));
        assertEquals(16, BigIntegerMath.log2(new BigInteger("132"), RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.UNNECESSARY));
        assertEquals(9, BigIntegerMath.log10(new BigInteger("1000000000"), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("6"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("132"), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("316227"), BigIntegerMath.sqrt(new BigInteger("99999999"), RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("10"), BigIntegerMath.divide(new BigInteger("20"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("20"), new BigInteger("4"), RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
        assertEquals(new BigInteger("1307674368000"), BigIntegerMath.factorial(15));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("36"), BigIntegerMath.binomial(8, 3));
        assertEquals(new BigInteger("84"), BigIntegerMath.binomial(9, 3));
    }
}