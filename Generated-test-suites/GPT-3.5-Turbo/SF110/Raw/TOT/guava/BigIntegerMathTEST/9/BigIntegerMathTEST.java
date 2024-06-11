package com.google.common.math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(0)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(7), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(7), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.HALF_EVEN));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.FLOOR));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.HALF_UP));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.HALF_DOWN));
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.HALF_EVEN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(5), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(5), RoundingMode.CEILING));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(8), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(25), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(25), RoundingMode.HALF_DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(25), RoundingMode.HALF_EVEN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(50), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(50), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(80), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
        BigInteger largeNum = BigInteger.valueOf(10).pow(50);
        assertEquals(50, BigIntegerMath.log10(largeNum, RoundingMode.UP));
        assertEquals(50, BigIntegerMath.log10(largeNum, RoundingMode.CEILING));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(7), BigIntegerMath.sqrt(BigInteger.valueOf(49), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.TEN, BigIntegerMath.sqrt(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2),
            RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(8), BigInteger.valueOf(2),
            RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(3, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(21), BigIntegerMath.binomial(7, 2));
        assertEquals(BigInteger.valueOf(56), BigIntegerMath.binomial(8, 3));
    }
}