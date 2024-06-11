package com.google.common.math;
import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
    }

    @Test
    public void testLog2() {
        assertEquals(2, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("10"), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(new BigInteger("5"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("50"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.HALF_EVEN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("33"), new BigInteger("11"), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("252"), BigIntegerMath.binomial(10, 5));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
    }
  
}