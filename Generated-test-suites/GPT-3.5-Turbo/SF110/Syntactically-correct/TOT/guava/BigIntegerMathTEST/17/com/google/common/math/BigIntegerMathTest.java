import com.google.common.math.BigIntegerMath;
import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Typical use case
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));

        // Edge cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1024)));

        // Error scenarios
        assertTrue(!BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-8)));
        assertTrue(!BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertTrue(!BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(100)));
    }

    @Test
    public void testLog2() {
        // Typical use case
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));

        // Edge cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.UNNECESSARY));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.log2(BigInteger.valueOf(-8), RoundingMode.UNNECESSARY);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testLog10() {
        // Typical use case
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));

        // Edge cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.log10(BigInteger.valueOf(-8), RoundingMode.UNNECESSARY);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testSqrt() {
        // Typical use case
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));

        // Edge cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(81), BigIntegerMath.sqrt(BigInteger.valueOf(6561), RoundingMode.UNNECESSARY));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(-8), RoundingMode.UNNECESSARY);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testDivide() {
        // Typical use case
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));

        // Edge cases
        assertEquals(BigInteger.valueOf(0), BigIntegerMath.divide(BigInteger.valueOf(0), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.valueOf(3), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(0), RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testFactorial() {
        // Typical use case
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));

        // Edge cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("1307674368000"), BigIntegerMath.factorial(15));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.factorial(-1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testBinomial() {
        // Typical use case
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));

        // Edge cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));

        // Error scenarios
        boolean thrown = false;
        try {
            BigIntegerMath.binomial(5, -1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}