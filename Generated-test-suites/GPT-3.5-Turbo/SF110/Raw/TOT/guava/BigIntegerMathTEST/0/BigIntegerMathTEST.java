import com.google.common.math.*;
import org.junit.jupiter.api.*;
import java.math.*;
import static org.junit.jupiter.api.Assertions.*;

class BigIntegerMathTest {

    // Test case for isPowerOfTwo method
    @Test
    void isPowerOfTwo() {
        // Typical use case
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));

        // Edge case with negative number
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-2")));

        // Edge case with zero
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.isPowerOfTwo(null);
        });
    }

    // Test case for log2 method
    @Test
    void log2() {
        // Typical use case with rounding mode DOWN
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.DOWN));

        // Typical use case with rounding mode UP
        assertEquals(3, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.UP));

        // Edge case with rounding mode UNNECESSARY
        assertThrows(ArithmeticException.class, () -> {
            BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY);
        });

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.log2(null, RoundingMode.DOWN);
        });
    }

    // Test case for log10 method
    @Test
    void log10() {
        // Typical use case with rounding mode FLOOR
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.FLOOR));

        // Typical use case with rounding mode CEILING
        assertEquals(1, BigIntegerMath.log10(new BigInteger("5"), RoundingMode.CEILING));

        // Edge case with rounding mode UNNECESSARY
        assertThrows(ArithmeticException.class, () -> {
            BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY);
        });

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.log10(null, RoundingMode.FLOOR);
        });
    }

    // Test case for sqrt method
    @Test
    void sqrt() {
        // Typical use case with rounding mode FLOOR
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.FLOOR));

        // Typical use case with rounding mode CEILING
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("5"), RoundingMode.CEILING));

        // Edge case with rounding mode UNNECESSARY
        assertThrows(ArithmeticException.class, () -> {
            BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY);
        });

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.sqrt(null, RoundingMode.FLOOR);
        });
    }

    // Test case for divide method
    @Test
    void divide() {
        // Typical use case with rounding mode DOWN
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.DOWN));

        // Typical use case with rounding mode UP
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("7"), new BigInteger("2"), RoundingMode.UP));

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.divide(null, new BigInteger("4"), RoundingMode.DOWN);
        });
    }

    // Test case for factorial method
    @Test
    void factorial() {
        // Typical use case
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));

        // Edge case with n = 0
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));

        // Error scenario with n < 0
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.factorial(-1);
        });
    }

    // Test case for binomial method
    @Test
    void binomial() {
        // Typical use case
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));

        // Edge case with n = k
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 5));

        // Error scenario with n < 0
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.binomial(-1, 1);
        });

        // Error scenario with k > n
        assertThrows(IllegalArgumentException.class, () -> {
            BigIntegerMath.binomial(5, 6);
        });
    }

    // Test case for fitsInLong method
    @Test
    void fitsInLong() {
        // Typical use case
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("15")));

        // Edge case with 64-bit number
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));

        // Error scenario with null input
        assertThrows(NullPointerException.class, () -> {
            BigIntegerMath.fitsInLong(null);
        });
    }
}