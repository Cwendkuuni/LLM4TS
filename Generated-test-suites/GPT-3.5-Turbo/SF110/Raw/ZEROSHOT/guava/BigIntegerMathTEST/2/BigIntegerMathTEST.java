import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("2");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("4");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("16");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("32");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("8");
        int expected = 3;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        x = new BigInteger("32");
        expected = 5;
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        x = new BigInteger("10");
        expected = 3;
        result = BigIntegerMath.log2(x, RoundingMode.CEILING);
        assertEquals(expected, result);
        
        x = new BigInteger("10");
        expected = 4;
        result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        int expected = 2;
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        x = new BigInteger("100");
        expected = 3;
        result = BigIntegerMath.log10(x, RoundingMode.UP);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("4");
        BigInteger expected = new BigInteger("2");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        x = new BigInteger("5");
        expected = new BigInteger("2");
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        x = new BigInteger("5");
        expected = new BigInteger("3");
        result = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        BigInteger expected = new BigInteger("5");
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        p = new BigInteger("10");
        q = new BigInteger("3");
        expected = new BigInteger("3");
        result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expected, result);
        
        p = new BigInteger("12");
        q = new BigInteger("5");
        expected = new BigInteger("2");
        result = BigIntegerMath.divide(p, q, RoundingMode.UP);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFactorial() {
        int n = 5;
        BigInteger expected = new BigInteger("120");
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);
        
        n = 10;
        expected = new BigInteger("3628800");
        result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);
    }
    
    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;
        BigInteger expected = new BigInteger("10");
        BigInteger result = BigIntegerMath.binomial(n, k);
        assertEquals(expected, result);
        
        n = 10;
        k = 3;
        expected = new BigInteger("120");
        result = BigIntegerMath.binomial(n, k);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFitsInLong() {
        BigInteger x = new BigInteger("100");
        assertTrue(BigIntegerMath.fitsInLong(x));
        
        x = new BigInteger("10000000000000000000000000000000000000000000000000000000000000000");
        assertFalse(BigIntegerMath.fitsInLong(x));
    }
}