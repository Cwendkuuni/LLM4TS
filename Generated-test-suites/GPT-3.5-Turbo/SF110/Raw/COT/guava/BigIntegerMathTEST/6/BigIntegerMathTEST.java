import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));
        
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("5")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("7")));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UNNECESSARY));
        assertEquals(16, BigIntegerMath.log2(new BigInteger("65536"), RoundingMode.UNNECESSARY));
        
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.FLOOR));
        assertEquals(16, BigIntegerMath.log2(new BigInteger("65536"), RoundingMode.FLOOR));
        
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(new BigInteger("16"), RoundingMode.UP));
        assertEquals(17, BigIntegerMath.log2(new BigInteger("65536"), RoundingMode.UP));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log10(new BigInteger("10000000000"), RoundingMode.UNNECESSARY));
        
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.FLOOR));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.FLOOR));
        assertEquals(10, BigIntegerMath.log10(new BigInteger("10000000000"), RoundingMode.FLOOR));
        
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UP));
        assertEquals(11, BigIntegerMath.log10(new BigInteger("10000000000"), RoundingMode.UP));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("256"), BigIntegerMath.sqrt(new BigInteger("65536"), RoundingMode.UNNECESSARY));
        
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("256"), BigIntegerMath.sqrt(new BigInteger("65536"), RoundingMode.FLOOR));
        
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.UP));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UP));
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.UP));
        assertEquals(new BigInteger("257"), BigIntegerMath.sqrt(new BigInteger("65536"), RoundingMode.UP));
    }
    
    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("5"), new BigInteger("2"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("5"), new BigInteger("2"), RoundingMode.CEILING));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("2"), RoundingMode.HALF_UP));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("720"), BigIntegerMath.factorial(6));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 5));
        assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        assertEquals(new BigInteger("120"), BigIntegerMath.binomial(10, 3));
        assertEquals(new BigInteger("45"), BigIntegerMath.binomial(10, 7));
    }
}