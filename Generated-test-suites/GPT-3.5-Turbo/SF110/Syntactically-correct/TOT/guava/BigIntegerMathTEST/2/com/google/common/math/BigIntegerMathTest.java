import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;

public class BigIntegerMathTest {

    private static BigIntegerMath bigIntegerMath;

    @BeforeClass
    public static void setUp() {
        bigIntegerMath = new BigIntegerMath();
    }

    @Test
    public void testIsPowerOfTwo_typicalCase() {
        BigInteger x = BigInteger.valueOf(8);
        boolean result = bigIntegerMath.isPowerOfTwo(x);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsPowerOfTwo_zeroValue() {
        BigInteger x = BigInteger.ZERO;
        boolean result = bigIntegerMath.isPowerOfTwo(x);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsPowerOfTwo_negativeValue() {
        BigInteger x = BigInteger.valueOf(-16);
        boolean result = bigIntegerMath.isPowerOfTwo(x);
        Assert.assertFalse(result);
    }

    @Test
    public void testLog2_typicalCase() {
        BigInteger x = BigInteger.valueOf(16);
        int result = bigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
        Assert.assertEquals(4, result);
    }

    @Test
    public void testLog2_zeroValue() {
        BigInteger x = BigInteger.ZERO;
        try {
            bigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("x must be positive", e.getMessage());
        }
    }

    @Test
    public void testLog2_negativeValue() {
        BigInteger x = BigInteger.valueOf(-8);
        try {
            bigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("x must be positive", e.getMessage());
        }
    }

    @Test
    public void testLog10_typicalCase() {
        BigInteger x = BigInteger.valueOf(1000);
        int result = bigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
        Assert.assertEquals(3, result);
    }

    @Test
    public void testLog10_zeroValue() {
        BigInteger x = BigInteger.ZERO;
        try {
            bigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("x must be positive", e.getMessage());
        }
    }

    @Test
    public void testLog10_negativeValue() {
        BigInteger x = BigInteger.valueOf(-10);
        try {
            bigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("x must be positive", e.getMessage());
        }
    }

    @Test
    public void testSqrt_typicalCase() {
        BigInteger x = BigInteger.valueOf(16);
        BigInteger result = bigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
        Assert.assertEquals(BigInteger.valueOf(4), result);
    }

    @Test
    public void testSqrt_negativeValue() {
        BigInteger x = BigInteger.valueOf(-16);
        try {
            bigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("x must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testDivide_typicalCase() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger result = bigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
        Assert.assertEquals(BigInteger.valueOf(5), result);
    }

    @Test
    public void testDivide_zeroDivisor() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.ZERO;
        try {
            bigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
            Assert.fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            Assert.assertEquals("Division by zero", e.getMessage());
        }
    }

    @Test
    public void testFactorial_typicalCase() {
        BigInteger result = bigIntegerMath.factorial(5);
        Assert.assertEquals(BigInteger.valueOf(120), result);
    }

    @Test
    public void testFactorial_negativeValue() {
        try {
            bigIntegerMath.factorial(-5);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("n must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testBinomial_typicalCase() {
        BigInteger result = bigIntegerMath.binomial(5, 2);
        Assert.assertEquals(BigInteger.valueOf(10), result);
    }

    @Test
    public void testBinomial_kGreaterThanN() {
        try {
            bigIntegerMath.binomial(5, 6);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("k (6) > n (5)", e.getMessage());
        }
    }

}