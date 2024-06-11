import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_typicalCase() {
        BigInteger x = new BigInteger("16");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_zero() {
        BigInteger x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_negativeNumber() {
        BigInteger x = new BigInteger("-16");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2_RoundingModeUnnecessary_exactPowerOfTwo() {
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testLog2_RoundingModeDown_exactPowerOfTwo() {
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));
    }
    
    @Test
    public void testLog2_RoundingModeDown_nonExactPowerOfTwo() {
        BigInteger x = new BigInteger("15");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));
    }
    
    @Test
    public void testLog2_RoundingModeUp_exactPowerOfTwo() {
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UP));
    }
    
    @Test
    public void testLog2_RoundingModeUp_nonExactPowerOfTwo() {
        BigInteger x = new BigInteger("15");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UP));
    }
    
    @Test
    public void testLog2_RoundingModeCeiling_exactPowerOfTwo() {
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.CEILING));
    }
    
    @Test
    public void testLog2_RoundingModeCeiling_nonExactPowerOfTwo() {
        BigInteger x = new BigInteger("15");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10_RoundingModeUnnecessary_fitsInLong() {
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testLog10_RoundingModeDown_fitsInLong() {
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN));
    }
    
    @Test
    public void testLog10_RoundingModeUp_fitsInLong() {
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UP));
    }
    
    @Test
    public void testLog10_RoundingModeCeiling_fitsInLong() {
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10_RoundingModeFloor_fitsInLong() {
        BigInteger x = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.FLOOR));
    }
    
    @Test
    public void testSqrt_RoundingModeUnnecessary_fitsInLong() {
        BigInteger x = new BigInteger("100");
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testSqrt_RoundingModeDown_fitsInLong() {
        BigInteger x = new BigInteger("100");
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }
    
    @Test
    public void testSqrt_RoundingModeUp_fitsInLong() {
        BigInteger x = new BigInteger("100");
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UP));
    }
    
    @Test
    public void testSqrt_RoundingModeCeiling_fitsInLong() {
        BigInteger x = new BigInteger("100");
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide_typicalCase() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        BigInteger expected = new BigInteger("5");
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testFactorial_typicalCase() {
        int n = 5;
        BigInteger expected = new BigInteger("120");
        assertEquals(expected, BigIntegerMath.factorial(n));
    }
    
    @Test
    public void testBinomial_typicalCase() {
        int n = 5;
        int k = 3;
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBinomial_invalidArguments_kGreaterThanN() {
        int n = 5;
        int k = 6;
        BigIntegerMath.binomial(n, k);
    }

}