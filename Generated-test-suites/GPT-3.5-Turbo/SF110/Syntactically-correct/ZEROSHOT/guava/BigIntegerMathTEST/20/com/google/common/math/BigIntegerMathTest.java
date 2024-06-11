import com.google.common.math.BigIntegerMath;
import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(64)));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
    }

    @Test
    public void testLog2() {
        Assert.assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        Assert.assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        Assert.assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        Assert.assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        Assert.assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        Assert.assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        Assert.assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        Assert.assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        Assert.assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
    }

    @Test
    public void testBinomial() {
        Assert.assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        Assert.assertEquals(BigInteger.valueOf(126), BigIntegerMath.binomial(9, 4));
    }
}