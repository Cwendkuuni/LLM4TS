import static org.junit.Assert.*;

import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
    }
    
    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10() {
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.CEILING));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(5), RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
    }
}