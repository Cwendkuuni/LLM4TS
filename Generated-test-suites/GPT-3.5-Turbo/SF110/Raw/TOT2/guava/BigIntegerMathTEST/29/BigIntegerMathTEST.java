import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(11), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.sqrt(BigInteger.valueOf(39), RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
    }

}