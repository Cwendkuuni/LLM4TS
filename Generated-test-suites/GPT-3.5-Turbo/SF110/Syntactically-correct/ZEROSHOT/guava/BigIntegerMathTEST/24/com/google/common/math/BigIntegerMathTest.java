import static org.junit.Assert.*;
import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        BigInteger y = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(y));
    }
    
    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
        
        BigInteger y = new BigInteger("15");
        assertEquals(3, BigIntegerMath.log2(y, RoundingMode.UP));
    }
    
    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
        
        BigInteger y = new BigInteger("1234");
        assertEquals(3, BigIntegerMath.log10(y, RoundingMode.UP));
    }
    
    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("16");
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
        
        BigInteger y = new BigInteger("15");
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(y, RoundingMode.FLOOR));
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p, q, RoundingMode.DOWN));
        
        BigInteger x = new BigInteger("100");
        BigInteger y = new BigInteger("3");
        assertEquals(new BigInteger("33"), BigIntegerMath.divide(x, y, RoundingMode.UP));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
    }
}