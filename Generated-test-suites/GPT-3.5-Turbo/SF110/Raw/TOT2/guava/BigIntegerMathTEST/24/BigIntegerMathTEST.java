import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("15"), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.TEN, BigIntegerMath.binomial(10, 1));
        assertEquals(new BigInteger("120"), BigIntegerMath.binomial(10, 3));
    }

    // Test edge cases for exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLog10RoundingMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSqrtRoundingMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY);
    }

}