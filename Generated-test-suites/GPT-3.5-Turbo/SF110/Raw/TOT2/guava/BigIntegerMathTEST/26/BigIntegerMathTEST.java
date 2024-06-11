import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(5), BigInteger.valueOf(2), RoundingMode.FLOOR));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.binomial(5, 3));
    }
}