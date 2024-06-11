import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("10"), RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(19, BigIntegerMath.log10(new BigInteger("10000000000000000000"), RoundingMode.HALF_EVEN));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("8"), new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.UP));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }
}