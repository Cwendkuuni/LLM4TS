import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
    }

    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(7), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(7), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(3), RoundingMode.UP));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
    }
}