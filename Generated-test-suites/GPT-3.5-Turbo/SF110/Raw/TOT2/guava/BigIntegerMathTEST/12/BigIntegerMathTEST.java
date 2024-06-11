import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("256")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(8, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.FLOOR));
        assertEquals(9, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.CEILING));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_UP));
        assertEquals(new BigInteger("0"), BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("479001600"), BigIntegerMath.factorial(12));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("330"), BigIntegerMath.binomial(11, 5));
        assertEquals(new BigInteger("1144066"), BigIntegerMath.binomial(52, 10));
    }
}