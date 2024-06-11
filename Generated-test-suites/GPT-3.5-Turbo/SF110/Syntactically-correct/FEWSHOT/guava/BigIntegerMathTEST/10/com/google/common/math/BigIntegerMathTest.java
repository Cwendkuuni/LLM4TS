
package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("20"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("200"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(new BigInteger("25"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("0"), BigIntegerMath.divide(new BigInteger("0"), new BigInteger("5"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1, 1));
        assertEquals(new BigInteger("3"), BigIntegerMath.binomial(3, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
}

