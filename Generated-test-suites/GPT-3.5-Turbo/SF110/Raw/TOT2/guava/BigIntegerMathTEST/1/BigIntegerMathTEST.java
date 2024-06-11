import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.UP));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.TEN, BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
    }
}