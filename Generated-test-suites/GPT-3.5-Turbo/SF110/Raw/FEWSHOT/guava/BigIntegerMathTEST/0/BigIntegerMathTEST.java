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
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));
        
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-1")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("9")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.HALF_UP));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testLog2WithRoundingUnnecessary() {
        BigIntegerMath.log2(new BigInteger("3"), RoundingMode.UNNECESSARY);
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.HALF_UP));
    }

    @Test(expected = ArithmeticException.class)
    public void testLog10WithRoundingUnnecessary() {
        BigIntegerMath.log10(new BigInteger("3"), RoundingMode.UNNECESSARY);
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.HALF_UP));
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSqrtWithRoundingUnnecessary() {
        BigIntegerMath.sqrt(new BigInteger("2"), RoundingMode.UNNECESSARY);
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("3"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("1"), BigIntegerMath.divide(new BigInteger("5"), new BigInteger("5"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("1"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("7"), RoundingMode.HALF_UP));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));   
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
        assertEquals(new BigInteger("20"), BigIntegerMath.binomial(6, 3));
        assertEquals(new BigInteger("35"), BigIntegerMath.binomial(7, 3));
    }
}