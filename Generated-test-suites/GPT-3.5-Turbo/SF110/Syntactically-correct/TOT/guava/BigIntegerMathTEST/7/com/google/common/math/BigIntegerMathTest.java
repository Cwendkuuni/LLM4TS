import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo_positivePower_returnsTrue() {
        BigInteger x = BigInteger.valueOf(64);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_negativePower_returnsFalse() {
        BigInteger x = BigInteger.valueOf(-64);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_zero_returnsFalse() {
        BigInteger x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2_roundingModeUnnecessary_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(8);
        int expected = 3;
        int actual = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog2_roundingModeDown_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(8);
        int expected = 3;
        int actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog2_roundingModeUp_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(8);
        int expected = 3;
        int actual = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog2_roundingModeFloor_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(8);
        int expected = 3;
        int actual = BigIntegerMath.log2(x, RoundingMode.FLOOR);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog10_roundingModeUnnecessary_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(100);
        int expected = 2;
        int actual = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog10_roundingModeDown_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(100);
        int expected = 2;
        int actual = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog10_roundingModeUp_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(100);
        int expected = 2;
        int actual = BigIntegerMath.log10(x, RoundingMode.UP);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLog10_roundingModeFloor_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(100);
        int expected = 2;
        int actual = BigIntegerMath.log10(x, RoundingMode.FLOOR);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSqrt_roundingModeUnnecessary_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(25);
        BigInteger expected = BigInteger.valueOf(5);
        BigInteger actual = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSqrt_roundingModeDown_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(25);
        BigInteger expected = BigInteger.valueOf(5);
        BigInteger actual = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSqrt_roundingModeUp_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(25);
        BigInteger expected = BigInteger.valueOf(5);
        BigInteger actual = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSqrt_roundingModeFloor_returnsCorrectValue() {
        BigInteger x = BigInteger.valueOf(25);
        BigInteger expected = BigInteger.valueOf(5);
        BigInteger actual = BigIntegerMath.sqrt(x, RoundingMode.FLOOR);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFactorial_negativeValue_throwsException() {
        int n = -1;
        try {
            BigIntegerMath.factorial(n);
            fail("Expected Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("n must be non-negative", e.getMessage());
        }
    }
    
    @Test
    public void testFactorial_zero_returnsOne() {
        int n = 0;
        BigInteger expected = BigInteger.ONE;
        BigInteger actual = BigIntegerMath.factorial(n);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testBinomial_negativeNValue_throwsException() {
        int n = -1;
        int k = 0;
        try {
            BigIntegerMath.binomial(n, k);
            fail("Expected Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("n must be non-negative", e.getMessage());
        }
    }
    
    @Test
    public void testBinomial_negativeKValue_throwsException() {
        int n = 1;
        int k = -1;
        try {
            BigIntegerMath.binomial(n, k);
            fail("Expected Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("k must be non-negative", e.getMessage());
        }
    }
    
    @Test
    public void testBinomial_KGreaterThanN_throwsException() {
        int n = 1;
        int k = 2;
        try {
            BigIntegerMath.binomial(n, k);
            fail("Expected Exception was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("k (2) > n (1)", e.getMessage());
        }
    }
    
    @Test
    public void testBinomial_smallValues_returnsCorrectValue() {
        int n = 5;
        int k = 2;
        BigInteger expected = BigInteger.valueOf(10);
        BigInteger actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFitsInLong_bitLengthLessThan63_returnsTrue() {
        BigInteger x = BigInteger.valueOf(127);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }
    
    @Test
    public void testFitsInLong_bitLengthEqualTo63_returnsTrue() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }
    
    @Test
    public void testFitsInLong_bitLengthGreaterThan63_returnsFalse() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        assertFalse(BigIntegerMath.fitsInLong(x));
    }
}