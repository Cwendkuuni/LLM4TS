import static org.junit.Assert.*;
import org.junit.Test;
import java.math.*;
import java.util.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TWO));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.UP));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(4), RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(45), BigIntegerMath.binomial(10, 2));
    }
}