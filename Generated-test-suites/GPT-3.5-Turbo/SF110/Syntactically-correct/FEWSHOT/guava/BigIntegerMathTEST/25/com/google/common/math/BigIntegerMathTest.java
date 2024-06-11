
package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class BigIntegerMathTest {
    
    private long MAX_LONG = Long.MAX_VALUE;

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1)));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2)));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4)));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8)));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1)));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10)));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100)));
        assertEquals(9, BigIntegerMath.log10(BigInteger.valueOf(1000000000)));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1)));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4)));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9)));
        assertEquals(BigInteger.valueOf(1000), BigIntegerMath.sqrt(BigInteger.valueOf(1000000)));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5)));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(25), BigInteger.valueOf(5)));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(100), BigInteger.valueOf(25)));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 3));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 4));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 5));
    }
    
    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(MAX_LONG)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(MAX_LONG).add(BigInteger.ONE)));
    }
}
