import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.FLOOR));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.FLOOR));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.TEN, RoundingMode.FLOOR));
        assertEquals(new BigInteger("10"), BigIntegerMath.divide(new BigInteger("100"), BigInteger.TEN, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.TEN, BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("120"), BigIntegerMath.binomial(10, 3));
    }
}