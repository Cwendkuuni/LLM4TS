import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
    }

    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.CEILING));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("6"), RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("6"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("6"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("5"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("6"), RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("6"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("6"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }

    @Test
    public void testFitsInLong() {
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));
    }

}