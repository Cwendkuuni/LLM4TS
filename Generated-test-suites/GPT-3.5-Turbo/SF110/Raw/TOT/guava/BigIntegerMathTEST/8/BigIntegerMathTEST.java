import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Typical case with positive power of two
        BigInteger x1 = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x1));
        
        // Typical case with negative power of two
        BigInteger x2 = BigInteger.valueOf(-8);
        assertFalse(BigIntegerMath.isPowerOfTwo(x2));
        
        // Edge case with zero
        BigInteger x3 = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x3));
        
        // Edge case with one
        BigInteger x4 = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x4));
    }
    
    @Test
    public void testLog2() {
        // Typical case with positive number and RoundingMode.UNNECESSARY
        BigInteger x1 = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x1, RoundingMode.UNNECESSARY));
        
        // Typical case with positive number and RoundingMode.DOWN
        BigInteger x2 = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x2, RoundingMode.DOWN));
        
        // Typical case with positive number and RoundingMode.UP
        BigInteger x3 = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x3, RoundingMode.UP));
        
        // Typical case with positive number and RoundingMode.HALF_DOWN
        BigInteger x4 = BigInteger.valueOf(8);
        assertEquals(3, BigIntegerMath.log2(x4, RoundingMode.HALF_DOWN));
        
        // Typical case with negative number and RoundingMode.HALF_UP
        BigInteger x5 = BigInteger.valueOf(-2);
        assertEquals(-2, BigIntegerMath.log2(x5, RoundingMode.HALF_UP));
        
        // Error scenario with zero and RoundingMode.HALF_EVEN
        BigInteger x6 = BigInteger.ZERO;
        try {
            BigIntegerMath.log2(x6, RoundingMode.HALF_EVEN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testLog10() {
        // Typical case with positive number and RoundingMode.UNNECESSARY
        BigInteger x1 = BigInteger.valueOf(10);
        assertEquals(1, BigIntegerMath.log10(x1, RoundingMode.UNNECESSARY));
        
        // Typical case with positive number and RoundingMode.FLOOR
        BigInteger x2 = BigInteger.valueOf(100);
        assertEquals(2, BigIntegerMath.log10(x2, RoundingMode.FLOOR));
        
        // Typical case with zero and RoundingMode.HALF_DOWN
        BigInteger x3 = BigInteger.ZERO;
        try {
            BigIntegerMath.log10(x3, RoundingMode.HALF_DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSqrt() {
        // Typical case with positive number and RoundingMode.UNNECESSARY
        BigInteger x1 = BigInteger.valueOf(64);
        assertEquals(BigInteger.valueOf(8), BigIntegerMath.sqrt(x1, RoundingMode.UNNECESSARY));
        
        // Typical case with positive number and RoundingMode.FLOOR
        BigInteger x2 = BigInteger.valueOf(100);
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.sqrt(x2, RoundingMode.FLOOR));
        
        // Error scenario with negative number and RoundingMode.CEILING
        BigInteger x3 = BigInteger.valueOf(-4);
        try {
            BigIntegerMath.sqrt(x3, RoundingMode.CEILING);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDivide() {
        // Typical case with positive numbers and RoundingMode.UNNECESSARY
        BigInteger p1 = BigInteger.valueOf(10);
        BigInteger q1 = BigInteger.valueOf(2);
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(p1, q1, RoundingMode.UNNECESSARY));
        
        // Typical case with negative numbers and RoundingMode.DOWN
        BigInteger p2 = BigInteger.valueOf(-15);
        BigInteger q2 = BigInteger.valueOf(-2);
        assertEquals(BigInteger.valueOf(7), BigIntegerMath.divide(p2, q2, RoundingMode.DOWN));
        
        // Error scenario with zero and RoundingMode.HALF_UP
        BigInteger p3 = BigInteger.ZERO;
        BigInteger q3 = BigInteger.valueOf(2);
        try {
            BigIntegerMath.divide(p3, q3, RoundingMode.HALF_UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testFactorial() {
        // Typical case with non-negative number
        int n1 = 5;
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(n1));
        
        // Error scenario with negative number
        int n2 = -5;
        try {
            BigIntegerMath.factorial(n2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testBinomial() {
        // Typical case with non-negative numbers
        int n1 = 5;
        int k1 = 2;
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n1, k1));
        
        // Error scenario with negative number
        int n2 = 5;
        int k2 = -2;
        try {
            BigIntegerMath.binomial(n2, k2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Error scenario with k greater than n
        int n3 = 5;
        int k3 = 10;
        try {
            BigIntegerMath.binomial(n3, k3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}