
import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("4");
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));

        BigInteger x2 = new BigInteger("5");
        assertFalse(BigIntegerMath.isPowerOfTwo(x2));
    }

    @Test
    public void testLog2() {
        BigInteger x1 = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = new BigInteger("10");
        assertEquals(3, BigIntegerMath.log2(x2, RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        BigInteger x1 = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = new BigInteger("1024");
        assertEquals(3, BigIntegerMath.log10(x2, RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        BigInteger x1 = new BigInteger("100");
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = new BigInteger("1024");
        assertEquals(new BigInteger("32"), BigIntegerMath.sqrt(x2, RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("5");
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(p, q, RoundingMode.DOWN));

        BigInteger p1 = new BigInteger("25");
        BigInteger q1 = new BigInteger("5");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p1, q1, RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        int n = 5;
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(n));
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(n, k));
    }

}
