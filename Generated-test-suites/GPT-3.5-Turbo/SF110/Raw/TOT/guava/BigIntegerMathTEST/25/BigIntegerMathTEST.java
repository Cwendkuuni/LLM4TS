import org.junit.*;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
  
  @Test
  public void testIsPowerOfTwo() {
    assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1"))); // typical case
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0"))); // edge case
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-8"))); // error scenario
  }
  
  @Test
  public void testLog2() {
    assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY)); // typical case
    assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.DOWN)); // edge case
    try {
      BigIntegerMath.log2(new BigInteger("-5"), RoundingMode.UNNECESSARY); // error scenario
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testLog10() {
    assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY)); // typical case
    assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN)); // edge case
    try {
      BigIntegerMath.log10(new BigInteger("-5"), RoundingMode.UNNECESSARY); // error scenario
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testSqrt() {
    assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY)); // typical case
    assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.DOWN)); // edge case
    try {
      BigIntegerMath.sqrt(new BigInteger("-5"), RoundingMode.UNNECESSARY); // error scenario
      fail("Expected IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  // ... additional test case methods for the other methods in BigIntegerMath class
  
}