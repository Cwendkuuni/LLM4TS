import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(500), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(500), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(15), RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("9"), new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("11"), new BigInteger("3"), RoundingMode.UP));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("11"), new BigInteger("3"), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
}