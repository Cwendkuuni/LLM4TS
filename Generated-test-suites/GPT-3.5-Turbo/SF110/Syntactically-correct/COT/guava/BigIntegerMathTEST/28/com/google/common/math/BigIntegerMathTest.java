import com.google.common.math.BigIntegerMath;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class BigIntegerMathTest {

    // isPowerOfTwo tests

    @Test
    public void testIsPowerOfTwo_PositivePowerOfTwo_ReturnsTrue() {
        BigInteger x = new BigInteger("8");
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        assertEquals(true, result);
    }

    @Test
    public void testIsPowerOfTwo_PositiveNonPowerOfTwo_ReturnsFalse() {
        BigInteger x = new BigInteger("9");
        boolean result = BigIntegerMath.isPowerOfTwo(x);
        assertEquals(false, result);
    }

    // log2 tests

    @Test
    public void testLog2_PositiveNumber_RoundingModeUnnecessary() {
        BigInteger x = new BigInteger("16");
        int result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
        assertEquals(4, result);
    }

    @Test
    public void testLog2_PositiveNumber_RoundingModeDown() {
        BigInteger x = new BigInteger("15");
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(3, result);
    }

    @Test
    public void testLog2_PositiveNumber_RoundingModeUp() {
        BigInteger x = new BigInteger("15");
        int result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(4, result);
    }

    // log10 tests

    @Test
    public void testLog10_PositiveNumber_RoundingModeUnnecessary() {
        BigInteger x = new BigInteger("1000");
        int result = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
        assertEquals(3, result);
    }

    @Test
    public void testLog10_PositiveNumber_RoundingModeDown() {
        BigInteger x = new BigInteger("999");
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(2, result);
    }

    @Test
    public void testLog10_PositiveNumber_RoundingModeUp() {
        BigInteger x = new BigInteger("999");
        int result = BigIntegerMath.log10(x, RoundingMode.UP);
        assertEquals(3, result);
    }

    // sqrt tests

    @Test
    public void testSqrt_PositiveNumber_RoundingModeUnnecessary() {
        BigInteger x = new BigInteger("9");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
        assertEquals(new BigInteger("3"), result);
    }

    @Test
    public void testSqrt_PositiveNumber_RoundingModeDown() {
        BigInteger x = new BigInteger("8");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    public void testSqrt_PositiveNumber_RoundingModeUp() {
        BigInteger x = new BigInteger("8");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(new BigInteger("3"), result);
    }

    // divide tests

    @Test
    public void testDivide_PositiveNumbers_RoundingModeUnnecessary() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("5");
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    public void testDivide_PositiveNumbers_RoundingModeDown() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("3");
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(new BigInteger("3"), result);
    }

    // factorial tests

    @Test
    public void testFactorial_NonNegativeInteger_ReturnsCorrectResult() {
        int n = 5;
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(new BigInteger("120"), result);
    }

    // binomial tests

    @Test
    public void testBinomial_ValidValues_ReturnsCorrectResult() {
        int n = 5;
        int k = 2;
        BigInteger result = BigIntegerMath.binomial(n, k);
        assertEquals(new BigInteger("10"), result);
    }

    // fitsInLong tests

    @Test
    public void testFitsInLong_PositiveNumber_ReturnsTrue() {
        BigInteger x = new BigInteger("100");
        boolean result = BigIntegerMath.fitsInLong(x);
        assertEquals(true, result);
    }

    @Test
    public void testFitsInLong_LargePositiveNumber_ReturnsFalse() {
        BigInteger x = new BigInteger("1000000000000000000");
        boolean result = BigIntegerMath.fitsInLong(x);
        assertEquals(false, result);
    }

}