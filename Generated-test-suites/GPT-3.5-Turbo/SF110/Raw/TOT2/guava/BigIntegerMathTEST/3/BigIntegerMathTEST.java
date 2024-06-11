import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("15")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("6"), BigIntegerMath.divide(new BigInteger("13"), new BigInteger("2"), RoundingMode.UP));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
}