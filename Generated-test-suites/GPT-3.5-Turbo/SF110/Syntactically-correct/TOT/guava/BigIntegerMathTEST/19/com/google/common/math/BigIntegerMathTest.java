import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_TypicalCase() {
        BigInteger x = BigInteger.valueOf(8);
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPowerOfTwo_NotPowerOfTwo() {
        BigInteger x = BigInteger.valueOf(10);
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsPowerOfTwo_NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-16);
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        Assert.assertFalse(result);
    }

    @Test (expected = NullPointerException.class)
    public void testIsPowerOfTwo_NullInput() {
        BigIntegerMath.isPowerOfTwo(null);
    }

    @Test
    public void testLog2_TypicalCase() {
        BigInteger x = BigInteger.valueOf(16);
        int expectedResult = 4;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLog2_NotPowerOfTwo() {
        BigInteger x = BigInteger.valueOf(10);
        int expectedResult = 3;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLog2_NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-8);
        int expectedResult = 2;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test (expected = NullPointerException.class)
    public void testLog2_NullInput() {
        BigIntegerMath.log2(null, RoundingMode.DOWN);
    }

    @Test
    public void testLog10_TypicalCase() {
        BigInteger x = BigInteger.valueOf(1000);
        int expectedResult = 3;
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLog10_LessThan10() {
        BigInteger x = BigInteger.valueOf(8);
        int expectedResult = 0;
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testLog10_NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-10000);
        int expectedResult = 4;
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test (expected = NullPointerException.class)
    public void testLog10_NullInput() {
        BigIntegerMath.log10(null, RoundingMode.DOWN);
    }

    @Test
    public void testSqrt_TypicalCase() {
        BigInteger x = BigInteger.valueOf(16);
        BigInteger expectedResult = BigInteger.valueOf(4);
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSqrt_NotPerfectSquare() {
        BigInteger x = BigInteger.valueOf(10);
        BigInteger expectedResult = BigInteger.valueOf(3);
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSqrt_NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-16);
        BigInteger expectedResult = BigInteger.valueOf(0);
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test (expected = NullPointerException.class)
    public void testSqrt_NullInput() {
        BigIntegerMath.sqrt(null, RoundingMode.DOWN);
    }

    @Test
    public void testDivide_TypicalCase() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger expectedResult = BigInteger.valueOf(5);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDivide_LargeNumbers() {
        BigInteger p = BigInteger.valueOf(1000000);
        BigInteger q = BigInteger.valueOf(1000);
        BigInteger expectedResult = BigInteger.valueOf(1000);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDivide_DivisionByZero() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.ZERO;
        try {
            BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Test passed
        }
    }

    @Test
    public void testFactorial_TypicalCase() {
        int n = 5;
        BigInteger expectedResult = BigInteger.valueOf(120);
        BigInteger result = BigIntegerMath.factorial(n);
        Assert.assertEquals(expectedResult, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFactorial_NegativeNumber() {
        int n = -5;
        BigIntegerMath.factorial(n);
    }

    @Test
    public void testFactorial_LargeNumber() {
        int n = 20;
        BigInteger expectedResult = new BigInteger("2432902008176640000");
        BigInteger result = BigIntegerMath.factorial(n);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testBinomial_TypicalCase() {
        int n = 5;
        int k = 2;
        BigInteger expectedResult = BigInteger.valueOf(10);
        BigInteger result = BigIntegerMath.binomial(n, k);
        Assert.assertEquals(expectedResult, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBinomial_NegativeNumbers() {
        int n = -5;
        int k = -2;
        BigIntegerMath.binomial(n, k);
    }

    @Test
    public void testBinomial_KGreaterThanN() {
        int n = 5;
        int k = 10;
        BigInteger expectedResult = BigInteger.ZERO;
        BigInteger result = BigIntegerMath.binomial(n, k);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testFitsInLong_TypicalCase() {
        BigInteger x = BigInteger.valueOf(100);
        boolean result = BigIntegerMath.fitsInLong(x);
        Assert.assertTrue(result);
    }

    @Test
    public void testFitsInLong_LargeNumber() {
        BigInteger x = new BigInteger("10000000000000000000000000000000000000000000000000");
        boolean result = BigIntegerMath.fitsInLong(x);
        Assert.assertFalse(result);
    }

    @Test
    public void testFitsInLong_NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-100);
        boolean result = BigIntegerMath.fitsInLong(x);
        Assert.assertTrue(result);
    }

}