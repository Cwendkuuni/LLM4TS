package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

  @Test
  public void testIsPowerOfTwo() {
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.TWO));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("300")));
  }

  @Test
  public void testLog2() {
    assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
    assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.FLOOR));
    assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.CEILING));
  }

  @Test
  public void testLog10() {
    assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.FLOOR));
    assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.CEILING));
    assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_UP));
  }

  @Test
  public void testSqrt() {
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.FLOOR));
    assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(17), RoundingMode.CEILING));
  }

  @Test
  public void testDivide() {
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.DOWN));
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(15), BigInteger.valueOf(4), RoundingMode.UP));
    assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(3), RoundingMode.HALF_DOWN));
  }

  @Test
  public void testFactorial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
  }

  @Test
  public void testBinomial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
    assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
    assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
  }

  @Test
  public void testFitsInLong() {
    assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
    assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
  }

}