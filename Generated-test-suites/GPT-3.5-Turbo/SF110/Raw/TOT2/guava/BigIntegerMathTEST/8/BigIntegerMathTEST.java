import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("10"), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.HALF_DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.CEILING));
        assertEquals(0, BigIntegerMath.log10(new BigInteger("0"), RoundingMode.HALF_UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.HALF_EVEN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.HALF_UP));
    }

    // Add more test cases for other methods such as divide, factorial, binomial, etc.

}