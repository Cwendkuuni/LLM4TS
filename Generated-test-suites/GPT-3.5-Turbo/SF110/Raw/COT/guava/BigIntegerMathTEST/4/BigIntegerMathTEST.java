
import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("-16");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("16");
        int expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
        
        x = new BigInteger("10");
        expected = 3;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.CEILING));
        
        x = new BigInteger("100");
        expected = 6;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.FLOOR));
    }
    
    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        int expected = 2;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
        
        x = new BigInteger("1000");
        expected = 2;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.CEILING));
        
        x = new BigInteger("10000");
        expected = 4;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.FLOOR));
    }
    
    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("16");
        BigInteger expected = new BigInteger("4");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
        
        x = new BigInteger("10");
        expected = new BigInteger("3");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.FLOOR));
        
        x = new BigInteger("100");
        expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.CEILING));
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        BigInteger expected = new BigInteger("5");
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
        
        p = new BigInteger("10");
        q = new BigInteger("3");
        expected = new BigInteger("3");
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.FLOOR));
        
        p = new BigInteger("10");
        q = new BigInteger("3");
        expected = new BigInteger("4");
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.CEILING));
    }
    
    @Test
    public void testFactorial() {
        int n = 5;
        BigInteger expected = new BigInteger("120");
        assertEquals(expected, BigIntegerMath.factorial(n));
        
        n = 10;
        expected = new BigInteger("3628800");
        assertEquals(expected, BigIntegerMath.factorial(n));
    }
    
    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.binomial(n, k));
        
        n = 10;
        k = 2;
        expected = new BigInteger("45");
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }
    
    @Test
    public void testFitsInLong() {
        BigInteger x = new BigInteger("10000");
        assertTrue(BigIntegerMath.fitsInLong(x));
        
        x = new BigInteger("1000000000000000000000000000000000000000000000000000000000000");
        assertFalse(BigIntegerMath.fitsInLong(x));
    }
}
