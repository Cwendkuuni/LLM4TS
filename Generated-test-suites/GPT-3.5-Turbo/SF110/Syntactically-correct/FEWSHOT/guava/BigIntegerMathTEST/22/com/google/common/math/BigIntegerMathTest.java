import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("9")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("17")));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.FLOOR));
        assertEquals(5, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log2(new BigInteger("65"), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("10"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("11"), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("15"), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("17"), RoundingMode.CEILING));
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(new BigInteger("25"), RoundingMode.HALF_UP));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("8"), new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("16"), new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("7"), BigIntegerMath.divide(new BigInteger("70"), new BigInteger("10"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("8"), BigIntegerMath.divide(new BigInteger("77"), new BigInteger("9"), RoundingMode.UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("22"), new BigInteger("10"), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 4));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 5));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775809")));
    }

}