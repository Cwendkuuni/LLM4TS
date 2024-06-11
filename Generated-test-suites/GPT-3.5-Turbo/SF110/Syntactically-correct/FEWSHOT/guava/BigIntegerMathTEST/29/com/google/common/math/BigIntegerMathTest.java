package com.google.common.math;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = BigInteger.valueOf(2);
        assertEquals(true, BigIntegerMath.isPowerOfTwo(x1));
        
        BigInteger x2 = BigInteger.valueOf(5);
        assertEquals(false, BigIntegerMath.isPowerOfTwo(x2));
        
        BigInteger x3 = BigInteger.ZERO;
        assertEquals(false, BigIntegerMath.isPowerOfTwo(x3));
    }
    
    @Test
    public void testLog2() {
        BigInteger x1 = BigInteger.valueOf(32);
        assertEquals(5, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = BigInteger.valueOf(100);
        assertEquals(6, BigIntegerMath.log2(x2, RoundingMode.HALF_UP));
        
        BigInteger x3 = BigInteger.valueOf(0);
        try {
            BigIntegerMath.log2(x3, RoundingMode.FLOOR);
        } catch (Exception e) {
            assertEquals("x cannot be 0", e.getMessage());
        }
    }
    
    @Test
    public void testLog10() {
        BigInteger x1 = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = BigInteger.TEN;
        assertEquals(1, BigIntegerMath.log10(x2, RoundingMode.HALF_UP));
        
        BigInteger x3 = BigInteger.ZERO;
        try {
            BigIntegerMath.log10(x3, RoundingMode.CEILING);
        } catch (Exception e) {
            assertEquals("x cannot be 0", e.getMessage());
        }
    }
    
    @Test
    public void testSqrt() {
        BigInteger x1 = BigInteger.valueOf(16);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = BigInteger.valueOf(5);
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(x2, RoundingMode.DOWN));
        
        BigInteger x3 = BigInteger.ZERO;
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(x3, RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
        
        BigInteger p2 = BigInteger.ZERO;
        BigInteger q2 = BigInteger.valueOf(5);
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(p2, q2, RoundingMode.HALF_UP));
    }
    
    @Test
    public void testFactorial() {
        int n = 5;
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(n));
        
        int n2 = -5;
        try {
            BigIntegerMath.factorial(n2);
        } catch (Exception e) {
            assertEquals("n cannot be negative", e.getMessage());
        }
        
    }
    
    @Test
    public void testBinomial() {
        int n = 6;
        int k = 3;
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(n, k));
        
        int n2 = -6;
        int k2 = 3;
        try {
            BigIntegerMath.binomial(n2, k2);
        } catch (Exception e) {
            assertEquals("n cannot be negative", e.getMessage());
        }
        
        int n3 = 6;
        int k3 = -3;
        try {
            BigIntegerMath.binomial(n3, k3);
        } catch (Exception e) {
            assertEquals("k cannot be negative", e.getMessage());
        }
        
        int n4 = 6;
        int k4 = 10;
        try {
            BigIntegerMath.binomial(n4, k4);
        } catch (Exception e) {
            assertEquals("k cannot be greater than n", e.getMessage());
        }
    }
    
}