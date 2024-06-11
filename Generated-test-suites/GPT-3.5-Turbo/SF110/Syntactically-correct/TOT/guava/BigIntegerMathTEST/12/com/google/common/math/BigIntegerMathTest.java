import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10000000000")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.CEILING));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(15), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.FLOOR));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(2), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(BigInteger.valueOf(100), RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(100), BigIntegerMath.divide(BigInteger.TEN.pow(3), BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.divide(BigInteger.TEN.pow(3), BigInteger.TEN, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(9), BigIntegerMath.divide(BigInteger.TEN.pow(3), BigInteger.valueOf(111), RoundingMode.HALF_UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("479001600"), BigIntegerMath.factorial(12));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.binomial(6, 2));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(10, 6));
    }
}