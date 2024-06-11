package com.google.common.math;

import org.junit.Test;
import org.junit.Before;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    private BigIntegerMath bigIntegerMath;

    @Before
    public void setUp() {
        bigIntegerMath = new BigIntegerMath();
    }

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("4");
        assertTrue(bigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_negative() {
        BigInteger x = new BigInteger("5");
        assertFalse(bigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("16");
        assertEquals(4, bigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog2_roundDown() {
        BigInteger x = new BigInteger("8");
        assertEquals(3, bigIntegerMath.log2(x, RoundingMode.DOWN));
    }

    @Test
    public void testLog2_roundUp() {
        BigInteger x = new BigInteger("9");
        assertEquals(4, bigIntegerMath.log2(x, RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        assertEquals(2, bigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10_roundDown() {
        BigInteger x = new BigInteger("100");
        assertEquals(2, bigIntegerMath.log10(x, RoundingMode.DOWN));
    }

    @Test
    public void testLog10_roundUp() {
        BigInteger x = new BigInteger("101");
        assertEquals(3, bigIntegerMath.log10(x, RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("100");
        assertEquals(new BigInteger("10"), bigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt_roundDown() {
        BigInteger x = new BigInteger("100");
        assertEquals(new BigInteger("10"), bigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }

    @Test
    public void testSqrt_roundUp() {
        BigInteger x = new BigInteger("101");
        assertEquals(new BigInteger("11"), bigIntegerMath.sqrt(x, RoundingMode.UP));
    }

    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        assertEquals(new BigInteger("5"), bigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        int n = 5;
        assertEquals(new BigInteger("120"), bigIntegerMath.factorial(n));
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        assertEquals(new BigInteger("10"), bigIntegerMath.binomial(n, k));
    }

    @Test
    public void testFitsInLong() {
        BigInteger x = new BigInteger("9");
        assertFalse(bigIntegerMath.fitsInLong(x));
    }
}