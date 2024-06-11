import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BigIntegerMathTest {

    // isPowerOfTwo

    @Test
    public void testIsPowerOfTwo_PositivePowerOfTwo_ReturnsTrue() {
        BigInteger x = BigInteger.valueOf(4);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_NotPowerOfTwo_ReturnsFalse() {
        BigInteger x = BigInteger.valueOf(7);
        assertTrue(!BigIntegerMath.isPowerOfTwo(x));
    }

    // log2

    @Test
    public void testLog2_PositiveNumber_RoundingModeUNNECESSARY() {
        BigInteger x = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog2_PositiveNumber_RoundingModeDOWN() {
        BigInteger x = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x, RoundingMode.DOWN));
    }

    @Test
    public void testLog2_PositiveNumber_RoundingModeUP() {
        BigInteger x = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x, RoundingMode.UP));
    }

    @Test
    public void testLog2_PositiveNumber_RoundingModeHALF_DOWN() {
        BigInteger x = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x, RoundingMode.HALF_DOWN));
    }

    // log10

    @Test
    public void testLog10_PositiveNumber_RoundingModeUNNECESSARY() {
        BigInteger x = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10_PositiveNumber_RoundingModeDOWN() {
        BigInteger x = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x, RoundingMode.DOWN));
    }

    @Test
    public void testLog10_PositiveNumber_RoundingModeUP() {
        BigInteger x = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x, RoundingMode.UP));
    }

    @Test
    public void testLog10_PositiveNumber_RoundingModeHALF_DOWN() {
        BigInteger x = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x, RoundingMode.HALF_DOWN));
    }

    // sqrt

    @Test
    public void testSqrt_PositiveNumber_RoundingModeUNNECESSARY() {
        BigInteger x = BigInteger.valueOf(16);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt_PositiveNumber_RoundingModeDOWN() {
        BigInteger x = BigInteger.valueOf(16);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }

    @Test
    public void testSqrt_PositiveNumber_RoundingModeUP() {
        BigInteger x = BigInteger.valueOf(16);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x, RoundingMode.UP));
    }

    // divide

    @Test
    public void testDivide_PositiveNumbers_RoundingModeDOWN() {
        BigInteger p = BigInteger.valueOf(8);
        BigInteger q = BigInteger.valueOf(3);
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(p, q, RoundingMode.DOWN));
    }

    // factorial

    @Test
    public void testFactorial_NegativeNumber_ExceptionThrown() {
        int n = -1;
        try {
            BigIntegerMath.factorial(n);
        } catch (IllegalArgumentException e) {
            assertEquals("n (".concat(String.valueOf(n)).concat(") must be non-negative"), e.getMessage());
        }
    }

    @Test
    public void testFactorial_Zero_ReturnsOne() {
        int n = 0;
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(n));
    }

    @Test
    public void testFactorial_PositiveNumber_ReturnsCorrectResult() {
        int n = 5;
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(n));
    }

    // binomial

    @Test
    public void testBinomial_NegativeNumbers_ExceptionThrown() {
        int n = -1;
        int k = -1;
        try {
            BigIntegerMath.binomial(n, k);
        } catch (IllegalArgumentException e) {
            assertEquals("n (".concat(String.valueOf(n)).concat(") must be non-negative"), e.getMessage());
        }
    }

    @Test
    public void testBinomial_kGreaterThan_n_ExceptionThrown() {
        int n = 5;
        int k = 6;
        try {
            BigIntegerMath.binomial(n, k);
        } catch (IllegalArgumentException e) {
            assertEquals("k (".concat(String.valueOf(k)).concat(") > n (").concat(String.valueOf(n)).concat(")"), e.getMessage());
        }
    }

    @Test
    public void testBinomial_nAndkAreEqual_ReturnsOne() {
        int n = 5;
        int k = 5;
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
    }

    @Test
    public void testBinomial_PositiveNumbers_ReturnsCorrectResult() {
        int n = 5;
        int k = 3;
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
    }

    // fitsInLong

    @Test
    public void testFitsInLong_NumberFitsInLong_ReturnsTrue() {
        BigInteger x = BigInteger.valueOf(1000000L);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testFitsInLong_NumberDoesNotFitInLong_ReturnsFalse() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        assertTrue(!BigIntegerMath.fitsInLong(x));
    }
}