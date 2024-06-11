import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_typicalCase() {
        // typical case
        BigInteger x = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_zero() {
        // edge case - zero
        BigInteger x = new BigInteger("0");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_negativeNumber() {
        // error scenario - negative number
        BigInteger x = new BigInteger("-4");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2_typicalCase() {
        // typical case
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog2_zero() {
        // edge case - zero
        BigInteger x = new BigInteger("0");
        try {
            BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // passed
        }
    }

    @Test
    public void testLog2_negativeNumber() {
        // error scenario - negative number
        BigInteger x = new BigInteger("-4");
        try {
            BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

    @Test
    public void testLog10_typicalCase() {
        // typical case
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10_zero() {
        // edge case - zero
        BigInteger x = new BigInteger("0");
        try {
            BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // passed
        }
    }

    @Test
    public void testLog10_negativeNumber() {
        // error scenario - negative number
        BigInteger x = new BigInteger("-4");
        try {
            BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

    @Test
    public void testSqrt_typicalCase() {
        // typical case
        BigInteger x = new BigInteger("25");
        BigInteger expected = new BigInteger("5");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt_negativeNumber() {
        // error scenario - negative number
        BigInteger x = new BigInteger("-25");
        try {
            BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

    @Test
    public void testDivide_typicalCase() {
        // typical case
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        BigInteger expected = new BigInteger("5");
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide_divisionByZero() {
        // error scenario - division by zero
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("0");
        try {
            BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // passed
        }
    }

    @Test
    public void testFactorial_typicalCase() {
        // typical case
        int n = 5;
        BigInteger expected = new BigInteger("120");
        assertEquals(expected, BigIntegerMath.factorial(n));
    }

    @Test
    public void testFactorial_negativeNumber() {
        // error scenario - negative number
        int n = -5;
        try {
            BigIntegerMath.factorial(n);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

    @Test
    public void testBinomial_typicalCase() {
        // typical case
        int n = 5;
        int k = 2;
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }

    @Test
    public void testBinomial_kGreaterThan_n() {
        // error scenario - k > n
        int n = 5;
        int k = 6;
        try {
            BigIntegerMath.binomial(n, k);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // passed
        }
    }

    @Test
    public void testFitsInLong_typicalCase() {
        // typical case
        BigInteger x = new BigInteger("123456");
        assertFalse(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testFitsInLong_maximumLongValue() {
        // edge case - maximum long value
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testFitsInLong_greaterThanMaximumLongValue() {
        // error scenario - greater than maximum long value
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        assertFalse(BigIntegerMath.fitsInLong(x));
    }
}