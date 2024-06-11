package com.google.common.math;
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("7")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("30"), new BigInteger("6"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("252"), BigIntegerMath.binomial(10, 5));
    }
}