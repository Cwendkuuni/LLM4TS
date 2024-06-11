import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void isPowerOfTwo_validInput_returnTrue() {
        BigInteger x = BigInteger.valueOf(8);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void isPowerOfTwo_zeroInput_returnFalse() {
        BigInteger x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void isPowerOfTwo_negativeInput_returnFalse() {
        BigInteger x = BigInteger.valueOf(-8);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void log2_positiveInputFloorMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(16);
        RoundingMode mode = RoundingMode.FLOOR;
        int expectedResult = 4;
        assertEquals(expectedResult, BigIntegerMath.log2(x, mode));
    }

    @Test
    public void log2_positiveInputCeilingMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(15);
        RoundingMode mode = RoundingMode.CEILING;
        int expectedResult = 4;
        assertEquals(expectedResult, BigIntegerMath.log2(x, mode));
    }

    @Test
    public void log2_negativeInput_returnIllegalArgumentException() {
        BigInteger x = BigInteger.valueOf(-16);
        RoundingMode mode = RoundingMode.FLOOR;
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.log2(x, mode);
        });
    }

    @Test
    public void log10_validInputFloorMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(1000);
        RoundingMode mode = RoundingMode.FLOOR;
        int expectedResult = 2;
        assertEquals(expectedResult, BigIntegerMath.log10(x, mode));
    }

    @Test
    public void log10_validInputCeilingMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(999);
        RoundingMode mode = RoundingMode.CEILING;
        int expectedResult = 3;
        assertEquals(expectedResult, BigIntegerMath.log10(x, mode));
    }

    @Test
    public void log10_negativeInput_returnIllegalArgumentException() {
        BigInteger x = BigInteger.valueOf(-1000);
        RoundingMode mode = RoundingMode.FLOOR;
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.log10(x, mode);
        });
    }

    @Test
    public void sqrt_validInputFloorMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(100);
        RoundingMode mode = RoundingMode.FLOOR;
        BigInteger expectedResult = BigInteger.valueOf(10);
        assertEquals(expectedResult, BigIntegerMath.sqrt(x, mode));
    }

    @Test
    public void sqrt_validInputCeilingMode_returnExpectedResult() {
        BigInteger x = BigInteger.valueOf(99);
        RoundingMode mode = RoundingMode.CEILING;
        BigInteger expectedResult = BigInteger.valueOf(10);
        assertEquals(expectedResult, BigIntegerMath.sqrt(x, mode));
    }

    @Test
    public void sqrt_negativeInput_returnIllegalArgumentException() {
        BigInteger x = BigInteger.valueOf(-100);
        RoundingMode mode = RoundingMode.FLOOR;
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.sqrt(x, mode);
        });
    }

    @Test
    public void divide_validInputs_returnExpectedResult() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        RoundingMode mode = RoundingMode.HALF_UP;
        BigInteger expectedResult = BigInteger.valueOf(5);
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, mode));
    }

    @Test
    public void factorial_validInput_returnExpectedResult() {
        int n = 5;
        BigInteger expectedResult = BigInteger.valueOf(120);
        assertEquals(expectedResult, BigIntegerMath.factorial(n));
    }

    @Test
    public void factorial_negativeInput_returnIllegalArgumentException() {
        int n = -5;
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.factorial(n);
        });
    }

    @Test
    public void binomial_validInputs_returnExpectedResult() {
        int n = 5;
        int k = 2;
        BigInteger expectedResult = BigInteger.valueOf(10);
        assertEquals(expectedResult, BigIntegerMath.binomial(n, k));
    }

    @Test
    public void binomial_invalidInput_throwIllegalArgumentException() {
        int n = 2;
        int k = 5;
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.binomial(n, k);
        });
    }

    @Test
    public void fitsInLong_validInput_returnTrue() {
        BigInteger x = BigInteger.valueOf(63);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void fitsInLong_invalidInput_returnFalse() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE + 1);
        assertFalse(BigIntegerMath.fitsInLong(x));
    }

}