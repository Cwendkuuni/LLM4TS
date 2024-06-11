import org.junit.Test;
import static org.junit.Assert.*;
import java.math.*;
import java.util.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_typicalCase() {
        BigInteger x = new BigInteger("2");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_edgeCase() {
        BigInteger x = new BigInteger("1");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_errorCase() {
        BigInteger x = new BigInteger("0");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2_typicalCase() {
        BigInteger x = new BigInteger("8");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(3, BigIntegerMath.log2(x, mode));
    }
    
    @Test
    public void testLog2_edgeCase() {
        BigInteger x = new BigInteger("1");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(0, BigIntegerMath.log2(x, mode));
    }
    
    @Test
    public void testLog2_errorCase() {
        BigInteger x = new BigInteger("-8");
        RoundingMode mode = RoundingMode.FLOOR;
        try {
            BigIntegerMath.log2(x, mode);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testLog10_typicalCase() {
        BigInteger x = new BigInteger("100");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(2, BigIntegerMath.log10(x, mode));
    }
    
    @Test
    public void testLog10_edgeCase() {
        BigInteger x = new BigInteger("1");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(0, BigIntegerMath.log10(x, mode));
    }
    
    @Test
    public void testLog10_errorCase() {
        BigInteger x = new BigInteger("-100");
        RoundingMode mode = RoundingMode.FLOOR;
        try {
            BigIntegerMath.log10(x, mode);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testSqrt_typicalCase() {
        BigInteger x = new BigInteger("16");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(x, mode));
    }
    
    @Test
    public void testSqrt_edgeCase() {
        BigInteger x = new BigInteger("1");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(x, mode));
    }
    
    @Test
    public void testSqrt_errorCase() {
        BigInteger x = new BigInteger("-16");
        RoundingMode mode = RoundingMode.FLOOR;
        try {
            BigIntegerMath.sqrt(x, mode);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    @Test
    public void testDivide_typicalCase() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("3");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(p, q, mode));
    }
    
    @Test
    public void testDivide_edgeCase() {
        BigInteger p = new BigInteger("1");
        BigInteger q = new BigInteger("1");
        RoundingMode mode = RoundingMode.FLOOR;
        assertEquals(new BigInteger("1"), BigIntegerMath.divide(p, q, mode));
    }
    
    @Test
    public void testDivide_errorCase() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("0");
        RoundingMode mode = RoundingMode.FLOOR;
        try {
            BigIntegerMath.divide(p, q, mode);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // pass
        }
    }
    
    @Test
    public void testFactorial_typicalCase() {
        int n = 5;
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(n));
    }
    
    @Test
    public void testFactorial_edgeCase() {
        int n = 0;
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(n));
    }
    
    @Test
    public void testBinomial_typicalCase() {
        int n = 5;
        int k = 2;
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(n, k));
    }
    
    @Test
    public void testBinomial_edgeCase() {
        int n = 0;
        int k = 0;
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(n, k));
    }
    
    @Test
    public void testBinomial_errorCase() {
        int n = 5;
        int k = 6;
        try {
            BigIntegerMath.binomial(n, k);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}