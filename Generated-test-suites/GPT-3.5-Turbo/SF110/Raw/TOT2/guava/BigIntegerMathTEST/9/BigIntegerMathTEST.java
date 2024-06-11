import org.junit.*;
import static org.junit.Assert.*;
import java.math.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("16");
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));
        
        BigInteger x2 = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(x2));
    }
    
    @Test
    public void testLog2() {
        BigInteger x1 = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = new BigInteger("10");
        assertEquals(3, BigIntegerMath.log2(x2, RoundingMode.FLOOR));
    }
    
    @Test
    public void testLog10() {
        BigInteger x1 = new BigInteger("1000");
        assertEquals(3, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));
        
        BigInteger x2 = new BigInteger("500");
        assertEquals(2, BigIntegerMath.log10(x2, RoundingMode.CEILING));
    }
    
    @Test
    public void testSqrt() {
        BigInteger x1 = new BigInteger("25");
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(x1, RoundingMode.HALF_UP));
        
        BigInteger x2 = new BigInteger("10");
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(x2, RoundingMode.DOWN));
    }
    
    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("100");
        BigInteger q = new BigInteger("25");
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(p, q, RoundingMode.DOWN));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
    }

}