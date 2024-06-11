import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.DOWN));
        assertEquals(8, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.DOWN));
        assertEquals(9, BigIntegerMath.log2(new BigInteger("257"), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("19"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("20"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("999"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.DOWN));
        assertEquals(9, BigIntegerMath.log10(new BigInteger("1000000000"), RoundingMode.DOWN));
        assertEquals(10, BigIntegerMath.log10(new BigInteger("1000000001"), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.DOWN));
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.DOWN));
        assertEquals(new BigInteger("31622"), BigIntegerMath.sqrt(new BigInteger("1000000000"), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("1"), BigIntegerMath.divide(new BigInteger("3"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("6"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.UP));
        assertEquals(new BigInteger("6"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.CEILING));
        assertEquals(new BigInteger("6"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("7"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("7"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(1, 1));
        assertEquals(new BigInteger("2"), BigIntegerMath.binomial(2, 1));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("252"), BigIntegerMath.binomial(10, 5));
    }
}