import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger powerOfTwo = new BigInteger("8");
        BigInteger notPowerOfTwo = new BigInteger("7");

        assertEquals(true, BigIntegerMath.isPowerOfTwo(powerOfTwo));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(notPowerOfTwo));
    }

    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("16");

        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.FLOOR));
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.CEILING));
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.HALF_DOWN));
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.HALF_UP));
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");

        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.HALF_DOWN));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("16");

        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.UP));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.CEILING));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.HALF_DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.HALF_UP));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");

        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        int n = 5;

        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(n));
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;

        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(n, k));
    }

    @Test
    public void testFitsInLong() {
        BigInteger bigInt = new BigInteger("10000000000000000");

        assertEquals(false, BigIntegerMath.fitsInLong(bigInt));
    }
}