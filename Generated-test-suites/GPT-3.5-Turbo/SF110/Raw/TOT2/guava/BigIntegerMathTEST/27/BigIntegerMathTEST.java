import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.DOWN));
        assertEquals(5, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("3"), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.UP));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("7"), new BigInteger("3"), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("720"), BigIntegerMath.factorial(6));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }

}