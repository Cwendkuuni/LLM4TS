package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.FLOOR));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.FLOOR));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("17"), RoundingMode.FLOOR));
    }
    
    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("5"), new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("17"), new BigInteger("4"), RoundingMode.FLOOR));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(new BigInteger("2"), BigIntegerMath.binomial(2, 1));
        assertEquals(new BigInteger("4"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
    }
    
    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger(Long.toString(Long.MAX_VALUE))));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger(Long.toString(Long.MAX_VALUE)).add(BigInteger.ONE)));
    }

}