import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

  private BigIntegerMath bigIntegerMath;

  @Before
  public void setUp() {
    bigIntegerMath = new BigIntegerMath();
  }

  @Test
  public void testIsPowerOfTwo_withPositivePowerOfTwo() {
    assertTrue(bigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
  }

  @Test
  public void testIsPowerOfTwo_withNegativePowerOfTwo() {
    assertFalse(bigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-16)));
  }

  @Test
  public void testIsPowerOfTwo_withZero() {
    assertFalse(bigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
  }

  @Test
  public void testLog2_withPositiveNumberAndRoundingModeUp() {
    assertEquals(4, bigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLog2_withNegativeNumber() {
    bigIntegerMath.log2(BigInteger.valueOf(-16), RoundingMode.UP);
  }

  @Test
  public void testLog10_withPositiveNumberAndRoundingModeDown() {
    assertEquals(1, bigIntegerMath.log10(BigInteger.valueOf(5), RoundingMode.DOWN));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLog10_withZero() {
    bigIntegerMath.log10(BigInteger.ZERO, RoundingMode.UNNECESSARY);
  }

  @Test
  public void testSqrt_withPositiveNumberAndRoundingModeCeiling() {
    assertEquals(BigInteger.valueOf(4), bigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.CEILING));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSqrt_withNegativeNumber() {
    bigIntegerMath.sqrt(BigInteger.valueOf(-16), RoundingMode.UP);
  }

  @Test
  public void testDivide_withPositiveNumbersAndRoundingModeDown() {
    assertEquals(BigInteger.valueOf(2), bigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.valueOf(2), RoundingMode.DOWN));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDivide_withZeroDivisor() {
    bigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.ZERO, RoundingMode.UNNECESSARY);
  }

  @Test
  public void testFactorial_withPositiveNumber() {
    assertEquals(BigInteger.valueOf(24), bigIntegerMath.factorial(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFactorial_withNegativeNumber() {
    bigIntegerMath.factorial(-4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBinomial_withNegativeIndex() {
    bigIntegerMath.binomial(4, -2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBinomial_withKGreaterThanN() {
    bigIntegerMath.binomial(2, 4);
  }

  @Test
  public void testBinomial_withValidInputs() {
    assertEquals(BigInteger.valueOf(6), bigIntegerMath.binomial(4, 2));
  }

  @Test
  public void testFitsInLong_withNumberFitsInLong() {
    assertTrue(bigIntegerMath.fitsInLong(BigInteger.valueOf(10)));
  }

  @Test
  public void testFitsInLong_withNumberDoesNotFitInLong() {
    assertFalse(bigIntegerMath.fitsInLong(BigInteger.valueOf(100000000000000000L)));
  }

}