
import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2L)));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3L)));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10L)));
    }
    
    @Test
    public void testLog2() {
        Assert.assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2L), RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(3L), RoundingMode.DOWN));
        Assert.assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4L), RoundingMode.DOWN));
    }
    
    @Test
    public void testLog10() {
        Assert.assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.UNNECESSARY));
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(12L), RoundingMode.FLOOR));
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(13L), RoundingMode.FLOOR));
    }
    
    @Test
    public void testSqrt() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(2L), BigIntegerMath.sqrt(BigInteger.valueOf(5L), RoundingMode.CEILING));
        Assert.assertEquals(BigInteger.valueOf(3L), BigIntegerMath.sqrt(BigInteger.valueOf(9L), RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.TEN, RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(5L), BigIntegerMath.divide(BigInteger.TEN, BigInteger.valueOf(2L), RoundingMode.HALF_UP));
    }
    
    @Test
    public void testFactorial() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        Assert.assertEquals(BigInteger.valueOf(120L), BigIntegerMath.factorial(5));
        Assert.assertEquals(BigInteger.valueOf(720L), BigIntegerMath.factorial(6));
    }
    
    @Test
    public void testBinomial() {
        Assert.assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        Assert.assertEquals(BigInteger.valueOf(5L), BigIntegerMath.binomial(5, 1));
        Assert.assertEquals(BigInteger.valueOf(10L), BigIntegerMath.binomial(5, 2));
        Assert.assertEquals(BigInteger.valueOf(252L), BigIntegerMath.binomial(10, 5));
    }
    
}
