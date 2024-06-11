import org.junit.Test;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));
        
        BigInteger x2 = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(x2));
    }
    
    @Test
    public void testLog2() {
        BigInteger x1 = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = new BigInteger("20");
        assertEquals(5, BigIntegerMath.log2(x2, RoundingMode.CEILING));
    }
    
    @Test
    public void testLog10() {
        BigInteger x1 = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = new BigInteger("150");
        assertEquals(2, BigIntegerMath.log10(x2, RoundingMode.DOWN));
        
        BigInteger x3 = new BigInteger("1000");
        assertEquals(3, BigIntegerMath.log10(x3, RoundingMode.UP));
    }
    
    @Test
    public void testSqrt() {
        BigInteger x1 = new BigInteger("16");
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = new BigInteger("20");
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x2, RoundingMode.DOWN));
        
        BigInteger x3 = new BigInteger("100");
        assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(x3, RoundingMode.UP));
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("2");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p, q, RoundingMode.HALF_UP));
        
        BigInteger p2 = new BigInteger("100");
        BigInteger q2 = new BigInteger("30");
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(p2, q2, RoundingMode.FLOOR));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(0, 0));
    }
    
}