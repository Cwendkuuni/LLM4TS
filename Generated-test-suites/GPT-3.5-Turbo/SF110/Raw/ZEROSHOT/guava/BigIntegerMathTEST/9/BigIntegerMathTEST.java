import org.junit.*;
import java.math.*;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.MathPreconditions;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("2");
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));

        BigInteger x2 = new BigInteger("4");
        assertTrue(BigIntegerMath.isPowerOfTwo(x2));

        BigInteger x3 = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x3));

        BigInteger x4 = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(x4));

        BigInteger x5 = new BigInteger("16");
        assertTrue(BigIntegerMath.isPowerOfTwo(x5));
    }

    @Test
    public void testLog2_RoundingMode() {
        BigInteger x1 = new BigInteger("2");
        assertEquals(1, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = new BigInteger("4");
        assertEquals(2, BigIntegerMath.log2(x2, RoundingMode.UNNECESSARY));

        BigInteger x3 = new BigInteger("8");
        assertEquals(3, BigIntegerMath.log2(x3, RoundingMode.UNNECESSARY));

        BigInteger x4 = new BigInteger("10");
        assertEquals(4, BigIntegerMath.log2(x4, RoundingMode.UNNECESSARY));

        BigInteger x5 = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x5, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10_RoundingMode() {
        BigInteger x1 = new BigInteger("2");
        assertEquals(0, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));

        BigInteger x2 = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(x2, RoundingMode.UNNECESSARY));

        BigInteger x3 = new BigInteger("1000");
        assertEquals(3, BigIntegerMath.log10(x3, RoundingMode.UNNECESSARY));

        BigInteger x4 = new BigInteger("1000000");
        assertEquals(6, BigIntegerMath.log10(x4, RoundingMode.UNNECESSARY));

        BigInteger x5 = new BigInteger("1000000000");
        assertEquals(9, BigIntegerMath.log10(x5, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt_RoundingMode() {
        BigInteger x1 = new BigInteger("4");
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(x1, RoundingMode.DOWN));

        BigInteger x2 = new BigInteger("9");
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(x2, RoundingMode.DOWN));

        BigInteger x3 = new BigInteger("16");
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x3, RoundingMode.DOWN));

        BigInteger x4 = new BigInteger("25");
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(x4, RoundingMode.DOWN));

        BigInteger x5 = new BigInteger("36");
        assertEquals(new BigInteger("6"), BigIntegerMath.sqrt(x5, RoundingMode.DOWN));
    }

    @Test
    public void testDivide_RoundingMode() {
        BigInteger p1 = new BigInteger("10");
        BigInteger q1 = new BigInteger("2");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p1, q1, RoundingMode.DOWN));

        BigInteger p2 = new BigInteger("100");
        BigInteger q2 = new BigInteger("3");
        assertEquals(new BigInteger("33"), BigIntegerMath.divide(p2, q2, RoundingMode.DOWN));

        BigInteger p3 = new BigInteger("1000");
        BigInteger q3 = new BigInteger("4");
        assertEquals(new BigInteger("250"), BigIntegerMath.divide(p3, q3, RoundingMode.DOWN));

        BigInteger p4 = new BigInteger("10000");
        BigInteger q4 = new BigInteger("5");
        assertEquals(new BigInteger("2000"), BigIntegerMath.divide(p4, q4, RoundingMode.DOWN));

        BigInteger p5 = new BigInteger("100000");
        BigInteger q5 = new BigInteger("6");
        assertEquals(new BigInteger("16666"), BigIntegerMath.divide(p5, q5, RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(3, 0));
        assertEquals(new BigInteger("3"), BigIntegerMath.binomial(3, 1));
        assertEquals(new BigInteger("3"), BigIntegerMath.binomial(3, 2));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(3, 3));
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
    }

    @Test
    public void testFitsInLong() {
        BigInteger x1 = new BigInteger("100");
        assertTrue(BigIntegerMath.fitsInLong(x1));

        BigInteger x2 = new BigInteger("1000000000000");
        assertFalse(BigIntegerMath.fitsInLong(x2));

        BigInteger x3 = new BigInteger("12345678901234567890");
        assertFalse(BigIntegerMath.fitsInLong(x3));

        BigInteger x4 = new BigInteger("18446744073709551616");
        assertFalse(BigIntegerMath.fitsInLong(x4));

        BigInteger x5 = new BigInteger("9223372036854775807");
        assertTrue(BigIntegerMath.fitsInLong(x5));
    }

}