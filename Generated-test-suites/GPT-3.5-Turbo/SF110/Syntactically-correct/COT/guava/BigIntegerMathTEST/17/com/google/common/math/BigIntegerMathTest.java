
import org.junit.*;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
  @Test
  public void testIsPowerOfTwo() {
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
  }

  @Test
  public void testLog2() {
    assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
    assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
    assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
    assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
  }

  @Test
  public void testLog10() {
    assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
    assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
    assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
    assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));
  }

  @Test
  public void testSqrt() {
    assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(7), RoundingMode.FLOOR));
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.FLOOR));
  }

  @Test
  public void testDivide() {
    assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.TEN, RoundingMode.UNNECESSARY));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.TEN, BigInteger.valueOf(5), RoundingMode.UNNECESSARY));
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.TEN, BigInteger.valueOf(3), RoundingMode.FLOOR));
  }

  @Test
  public void testFactorial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
    assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
  }

  @Test
  public void testBinomial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
    assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
    assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
  }

  @Test
  public void testFitsInLong() {
    assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
    assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)));
  }
}
