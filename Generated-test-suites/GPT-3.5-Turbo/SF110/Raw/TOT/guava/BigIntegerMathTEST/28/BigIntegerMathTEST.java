import org.junit.*;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger input1 = BigInteger.ZERO;
        BigInteger input2 = BigInteger.ONE;
        BigInteger input3 = BigInteger.valueOf(4);
        BigInteger input4 = BigInteger.valueOf(6);

        boolean result1 = BigIntegerMath.isPowerOfTwo(input1);
        boolean result2 = BigIntegerMath.isPowerOfTwo(input2);
        boolean result3 = BigIntegerMath.isPowerOfTwo(input3);
        boolean result4 = BigIntegerMath.isPowerOfTwo(input4);

        assertFalse(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertFalse(result4);
    }

    @Test
    public void testLog2() {
        BigInteger input1 = BigInteger.ONE;
        BigInteger input2 = BigInteger.valueOf(4);
        BigInteger input3 = BigInteger.valueOf(10);

        int result1 = BigIntegerMath.log2(input1, RoundingMode.UNNECESSARY);
        int result2 = BigIntegerMath.log2(input2, RoundingMode.UNNECESSARY);
        int result3 = BigIntegerMath.log2(input3, RoundingMode.UNNECESSARY);

        assertEquals(0, result1);
        assertEquals(2, result2);
        assertEquals(3, result3);
    }

    @Test
    public void testLog10() {
        BigInteger input1 = BigInteger.ONE;
        BigInteger input2 = BigInteger.valueOf(10);
        BigInteger input3 = BigInteger.valueOf(100);

        int result1 = BigIntegerMath.log10(input1, RoundingMode.UNNECESSARY);
        int result2 = BigIntegerMath.log10(input2, RoundingMode.UNNECESSARY);
        int result3 = BigIntegerMath.log10(input3, RoundingMode.UNNECESSARY);

        assertEquals(0, result1);
        assertEquals(1, result2);
        assertEquals(2, result3);
    }

    @Test
    public void testSqrt() {
        BigInteger input1 = BigInteger.ONE;
        BigInteger input2 = BigInteger.valueOf(4);
        BigInteger input3 = BigInteger.valueOf(10);

        BigInteger result1 = BigIntegerMath.sqrt(input1, RoundingMode.UNNECESSARY);
        BigInteger result2 = BigIntegerMath.sqrt(input2, RoundingMode.UNNECESSARY);
        BigInteger result3 = BigIntegerMath.sqrt(input3, RoundingMode.UNNECESSARY);

        assertEquals(BigInteger.ONE, result1);
        assertEquals(BigInteger.TWO, result2);
        assertEquals(BigInteger.valueOf(3), result3);
    }

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);

        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);

        assertEquals(BigInteger.valueOf(5), result);
    }

    @Test
    public void testFactorial() {
        int n = 5;

        BigInteger result = BigIntegerMath.factorial(n);

        assertEquals(BigInteger.valueOf(120), result);
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;

        BigInteger result = BigIntegerMath.binomial(n, k);

        assertEquals(BigInteger.valueOf(10), result);
    }

}