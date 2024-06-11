package com.google.common.math;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1024)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1001)));
    }
    
    @Test
    public void testLog2() {
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1001), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(32), BigIntegerMath.sqrt(BigInteger.valueOf(1024), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(31), BigIntegerMath.sqrt(BigInteger.valueOf(1001), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(0), BigIntegerMath.divide(BigInteger.valueOf(1), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(0), BigIntegerMath.divide(BigInteger.valueOf(0), BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 3));
    }
    
    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(1000000)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(1000000000000L)));
    }
}