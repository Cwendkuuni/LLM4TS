import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("15")));
    }

    @Test
    public void testLog2() {
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UNNECESSARY));
        assertEquals(8, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("3"), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.HALF_UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("3"), RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("15"), new BigInteger("3"), RoundingMode.UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("17"), new BigInteger("8"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("720"), BigIntegerMath.factorial(6));
        assertEquals(new BigInteger("5040"), BigIntegerMath.factorial(7));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("15"), BigIntegerMath.binomial(6, 2));
    }

}