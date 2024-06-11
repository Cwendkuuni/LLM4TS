package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("8");
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        assertTrue(result);
        
        x = new BigInteger("10");
        result = BigIntegerMath.isPowerOfTwo(x);
        assertFalse(result);
        
        x = new BigInteger("0");
        result = BigIntegerMath.isPowerOfTwo(x);
        assertFalse(result);
        
        x = new BigInteger("-16");
        result = BigIntegerMath.isPowerOfTwo(x);
        assertFalse(result);
    }
    
    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("10");
        int result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
        assertEquals(3, result);
        
        x = new BigInteger("15");
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(3, result);
        
        x = new BigInteger("15");
        result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(4, result);
        
        x = new BigInteger("0");
        try {
            result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
        } catch (Exception e) {
            assertTrue(e instanceof ArithmeticException);
        }
    }
    
    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        int result = BigIntegerMath.log10(x, RoundingMode.UP);
        assertEquals(2, result);
        
        x = new BigInteger("1000");
        result = BigIntegerMath.log10(x, RoundingMode.FLOOR);
        assertEquals(2, result);
        
        x = new BigInteger("0");
        try {
            result = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
        } catch (Exception e) {
            assertTrue(e instanceof ArithmeticException);
        }
    }
    
    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("16");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(new BigInteger("4"), result);
        
        x = new BigInteger("27");
        result = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(new BigInteger("6"), result);
        
        x = new BigInteger("0");
        result = BigIntegerMath.sqrt(x, RoundingMode.FLOOR);
        assertEquals(new BigInteger("0"), result);
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("5");
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(new BigInteger("2"), result);
        
        q = new BigInteger("-5");
        result = BigIntegerMath.divide(p, q, RoundingMode.UP);
        assertEquals(new BigInteger("-2"), result);
        
        p = new BigInteger("0");
        result = BigIntegerMath.divide(p, q, RoundingMode.FLOOR);
        assertEquals(new BigInteger("0"), result);
        
        p = new BigInteger("-15");
        result = BigIntegerMath.divide(p, q, RoundingMode.CEILING);
        assertEquals(new BigInteger("3"), result);
    }
    
    @Test
    public void testFactorial() {
        BigInteger result = BigIntegerMath.factorial(5);
        assertEquals(new BigInteger("120"), result);
        
        result = BigIntegerMath.factorial(0);
        assertEquals(new BigInteger("1"), result);
        
        result = BigIntegerMath.factorial(1);
        assertEquals(new BigInteger("1"), result);
    }
    
    @Test
    public void testBinomial() {
        BigInteger result = BigIntegerMath.binomial(5, 2);
        assertEquals(new BigInteger("10"), result);
        
        result = BigIntegerMath.binomial(0, 0);
        assertEquals(new BigInteger("1"), result);
        
        result = BigIntegerMath.binomial(6, 7);
        assertEquals(new BigInteger("0"), result);
    }
}