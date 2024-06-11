import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));

        BigInteger x2 = BigInteger.valueOf(18);
        assertFalse(BigIntegerMath.isPowerOfTwo(x2));

        BigInteger x3 = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x3));
    }

    @Test
    public void testLog2() {
        BigInteger x1 = BigInteger.valueOf(16);
        assertEquals(4, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = BigInteger.valueOf(17);
        assertEquals(4, BigIntegerMath.log2(x2, RoundingMode.UNNECESSARY));

        BigInteger x3 = BigInteger.valueOf(32);
        assertEquals(5, BigIntegerMath.log2(x3, RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        BigInteger x1 = BigInteger.valueOf(100);
        assertEquals(2, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = BigInteger.valueOf(150);
        assertEquals(2, BigIntegerMath.log10(x2, RoundingMode.UP));

        BigInteger x3 = BigInteger.valueOf(1000);
        assertEquals(3, BigIntegerMath.log10(x3, RoundingMode.FLOOR));
    }

    @Test
    public void testSqrt() {
        BigInteger x1 = BigInteger.valueOf(16);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = BigInteger.valueOf(18);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x2, RoundingMode.FLOOR));

        BigInteger x3 = BigInteger.valueOf(25);
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(x3, RoundingMode.CEILING));
    }

    @Test
    public void testDivide() {
        BigInteger p1 = BigInteger.valueOf(16);
        BigInteger q1 = BigInteger.valueOf(4);
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(p1, q1, RoundingMode.UNNECESSARY));

        BigInteger p2 = BigInteger.valueOf(18);
        BigInteger q2 = BigInteger.valueOf(5);
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(p2, q2, RoundingMode.CEILING));

        BigInteger p3 = BigInteger.valueOf(20);
        BigInteger q3 = BigInteger.valueOf(3);
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.divide(p3, q3, RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("30414093201713378043612608166064768844377641568960512000000000000"), BigIntegerMath.factorial(50));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(5, 1));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(10, 3));
    }
}