import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
  
  @Test
  public void testIsPowerOfTwo() {
    BigInteger x = new BigInteger("4");
    boolean result = BigIntegerMath.isPowerOfTwo(x);
    Assert.assertTrue(result);
  }
  
  @Test
  public void testLog2() {
    BigInteger x = new BigInteger("8");
    int expected = 3;
    int result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testLog10() {
    BigInteger x = new BigInteger("100");
    int expected = 2;
    int result = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testSqrt() {
    BigInteger x = new BigInteger("100");
    BigInteger expected = new BigInteger("10");
    BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testDivide() {
    BigInteger p = new BigInteger("10");
    BigInteger q = new BigInteger("5");
    BigInteger expected = new BigInteger("2");
    BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testFactorial() {
    int n = 5;
    BigInteger expected = new BigInteger("120");
    BigInteger result = BigIntegerMath.factorial(n);
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testBinomial() {
    int n = 5;
    int k = 2;
    BigInteger expected = new BigInteger("10");
    BigInteger result = BigIntegerMath.binomial(n, k);
    Assert.assertEquals(expected, result);
  }
  
}