import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.HALF_UP));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(84), BigIntegerMath.binomial(7, 3));
    }
}