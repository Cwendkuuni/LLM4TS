package com.google.common.math;

import org.junit.Test;
import org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("4");
        boolean result1 = BigIntegerMath.isPowerOfTwo(x1);
        assertEquals(true, result1);
        
        BigInteger x2 = new BigInteger("6");
        boolean result2 = BigIntegerMath.isPowerOfTwo(x2);
        assertEquals(false, result2);
    }

    @Test
    public void testLog2() {
        BigInteger x1 = new BigInteger("8");
        int result1 = BigIntegerMath.log2(x1, RoundingMode.FLOOR);
        assertEquals(3, result1);
        
        BigInteger x2 = new BigInteger("9");
        int result2 = BigIntegerMath.log2(x2, RoundingMode.CEILING);
        assertEquals(4, result2);
    }

    @Test
    public void testLog10() {
        BigInteger x1 = new BigInteger("100");
        int result1 = BigIntegerMath.log10(x1, RoundingMode.FLOOR);
        assertEquals(2, result1);
        
        BigInteger x2 = new BigInteger("1000");
        int result2 = BigIntegerMath.log10(x2, RoundingMode.CEILING);
        assertEquals(3, result2);
    }

    @Test
    public void testSqrt() {
        BigInteger x1 = new BigInteger("16");
        BigInteger result1 = BigIntegerMath.sqrt(x1, RoundingMode.FLOOR);
        assertEquals(new BigInteger("4"), result1);
        
        BigInteger x2 = new BigInteger("25");
        BigInteger result2 = BigIntegerMath.sqrt(x2, RoundingMode.CEILING);
        assertEquals(new BigInteger("5"), result2);
    }

    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(new BigInteger("5"), result);
    }

    @Test
    public void testFactorial() {
        int n = 5;
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(new BigInteger("120"), result);
    }

    @Test
    public void testBinomial() {
        int n = 6;
        int k = 2;
        BigInteger result = BigIntegerMath.binomial(n, k);
        assertEquals(new BigInteger("15"), result);
    }
}