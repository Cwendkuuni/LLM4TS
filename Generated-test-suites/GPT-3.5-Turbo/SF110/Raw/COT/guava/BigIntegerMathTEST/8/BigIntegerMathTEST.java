import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("123456")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("9"), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.HALF_EVEN));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.TEN, new BigInteger("5"), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.TEN, new BigInteger("3"), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("1307674368000"), BigIntegerMath.factorial(15));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.binomial(6, 2));
    }

    @Test
    public void testFitsInLong() {
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));
    }
}