import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(0)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-8)));
    }

    @Test
    public void testLog2() {
        assertEquals(BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN), 3);
        assertEquals(BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UP), 3);
        assertEquals(BigIntegerMath.log2(BigInteger.valueOf(10), RoundingMode.CEILING), 4);
        assertEquals(BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.FLOOR), 4);
        assertEquals(BigIntegerMath.log2(BigInteger.valueOf(6), RoundingMode.HALF_DOWN), 2);
    }

    @Test
    public void testLog10() {
        assertEquals(BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN), 2);
        assertEquals(BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP), 2);
        assertEquals(BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING), 4);
        assertEquals(BigIntegerMath.log10(BigInteger.valueOf(100000), RoundingMode.FLOOR), 5);
        assertEquals(BigIntegerMath.log10(BigInteger.valueOf(25), RoundingMode.HALF_DOWN), 1);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN), BigInteger.valueOf(3));
        assertEquals(BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.UP), BigInteger.valueOf(4));
        assertEquals(BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.CEILING), BigInteger.valueOf(5));
        assertEquals(BigIntegerMath.sqrt(BigInteger.valueOf(36), RoundingMode.FLOOR), BigInteger.valueOf(6));
        assertEquals(BigIntegerMath.sqrt(BigInteger.valueOf(20), RoundingMode.HALF_DOWN), BigInteger.valueOf(4));
    }

    @Test
    public void testDivide() {
        assertEquals(BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.DOWN),
                BigInteger.valueOf(5));
        assertEquals(BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UP),
                BigInteger.valueOf(4));
        assertEquals(BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(4), RoundingMode.CEILING),
                BigInteger.valueOf(3));
        assertEquals(BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.FLOOR),
                BigInteger.valueOf(2));
        assertEquals(BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(6), RoundingMode.HALF_DOWN),
                BigInteger.valueOf(1));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigIntegerMath.factorial(0), BigInteger.ONE);
        assertEquals(BigIntegerMath.factorial(1), BigInteger.ONE);
        assertEquals(BigIntegerMath.factorial(5), BigInteger.valueOf(120));
        assertEquals(BigIntegerMath.factorial(10), BigInteger.valueOf(3628800));
        assertEquals(BigIntegerMath.factorial(15), BigInteger.valueOf(1307674368000L));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigIntegerMath.binomial(5, 2), BigInteger.valueOf(10));
        assertEquals(BigIntegerMath.binomial(10, 5), BigInteger.valueOf(252));
        assertEquals(BigIntegerMath.binomial(15, 3), BigInteger.valueOf(455));
        assertEquals(BigIntegerMath.binomial(20, 10), BigInteger.valueOf(184756));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(63)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(9223372036854775808L)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(2147483647)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(0)));
    }
}