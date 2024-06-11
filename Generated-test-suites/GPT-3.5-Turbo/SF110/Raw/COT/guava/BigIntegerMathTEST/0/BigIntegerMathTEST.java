import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
    }

    @Test
    public void testLog2() {
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(4, 0));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)));
    }
}