package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("100")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.DOWN));
        assertEquals(7, BigIntegerMath.log2(new BigInteger("100"), RoundingMode.DOWN));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("9"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("99"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("999"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("9999"), RoundingMode.DOWN));
        assertEquals(16, BigIntegerMath.log10(new BigInteger("10000000000000000"), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("0"), BigIntegerMath.sqrt(new BigInteger("0"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.DOWN));
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("5"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("7"), RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 5));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("15"), BigIntegerMath.binomial(6, 2));
        assertEquals(new BigInteger("20"), BigIntegerMath.binomial(6, 3));
    }

}