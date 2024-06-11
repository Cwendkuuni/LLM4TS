package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    // Test for isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
    }

    // Test for log2 method
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(10), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(10), RoundingMode.HALF_DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        assertEquals(54, BigIntegerMath.log2(new BigInteger("18014398509481983"), RoundingMode.UNNECESSARY));
    }

    // Test for log10 method
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(11), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(13), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(14), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.CEILING));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.HALF_DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        assertEquals(16, BigIntegerMath.log10(new BigInteger("18014398509481983"), RoundingMode.UNNECESSARY));
    }

    // Test for sqrt method
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(13), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(14), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(1342097744),
                BigIntegerMath.sqrt(new BigInteger("18014398509481983"), RoundingMode.UNNECESSARY));
    }

    // Test for divide method
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.divide(BigInteger.valueOf(12), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(new BigInteger("23238975651"), new BigInteger("4647795130"), RoundingMode.UNNECESSARY));
    }

    // Test for factorial method
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("30414093201713378043612608166064768844377641568960512000000000000"), BigIntegerMath.factorial(50));
    }

    // Test for binomial method
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("30414093201713378043612608166064768844377641568960512000000000000"), BigIntegerMath.binomial(50, 25));
    }

    // Test for fitsInLong method
    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)));
    }

}