import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
    }
    
    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("15"), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("12"), RoundingMode.HALF_EVEN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("14"), RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.CEILING));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("15"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.UP));
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
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 5));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
    
    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775807")));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("0")));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("9223372036854775808")));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("-9223372036854775808")));
    }
}