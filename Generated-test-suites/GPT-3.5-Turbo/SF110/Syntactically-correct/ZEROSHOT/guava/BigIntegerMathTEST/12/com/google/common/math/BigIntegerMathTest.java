import org.junit.*;
import java.math.*;
import com.google.common.math.BigIntegerMath;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test for positive powers of 2
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));

        // Test for non-power of 2 numbers
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(0)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("1025")));
    }

    @Test
    public void testLog2() {
        // Test for log2 of power of 2 numbers
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.UNNECESSARY));

        // Test for log2 of non-power of 2 numbers
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(10, BigIntegerMath.log2(new BigInteger("1023"), RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        // Test for log10 of small numbers
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));

        // Test for log10 of large numbers
        assertEquals(1000, BigIntegerMath.log10(new BigInteger("10").pow(1000), RoundingMode.DOWN));
        assertEquals(1001, BigIntegerMath.log10(new BigInteger("2").pow(1000), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        // Test for sqrt of small numbers
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));

        // Test for sqrt of large numbers
        assertEquals(BigInteger.valueOf(100), BigIntegerMath.sqrt(new BigInteger("10000"), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(31622), BigIntegerMath.sqrt(new BigInteger("1000000000"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        // Test for divide of small numbers
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));

        // Test for divide of large numbers
        assertEquals(new BigInteger("33333333333333333333333333"), BigIntegerMath.divide(new BigInteger("100000000000000000000000000"), new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("100000000000000000000000000"), BigIntegerMath.divide(new BigInteger("100000000000000000000000000"), new BigInteger("1"), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        // Test for factorial of small numbers
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));

        // Test for factorial of large numbers
        assertEquals(new BigInteger("15511210043330985984000000"), BigIntegerMath.factorial(25));
        assertEquals(new BigInteger("30414093201713378043612608166064768844377641568960512000000000000"), BigIntegerMath.factorial(50));
    }

    @Test
    public void testBinomial() {
        // Test for binomial of small numbers
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));

        // Test for binomial of large numbers
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(252), BigIntegerMath.binomial(10, 5));
    }
}