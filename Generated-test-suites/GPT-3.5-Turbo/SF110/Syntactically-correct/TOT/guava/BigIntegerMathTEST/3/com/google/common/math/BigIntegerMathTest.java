import static org.junit.Assert.*;
import org.junit.Test;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        // Typical use cases
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));
        
        // Edge cases
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("0")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-2")));
        
        // Error scenarios
        try {
            BigIntegerMath.isPowerOfTwo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testLog2() {
        // Typical use cases
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(9, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.DOWN));
        
        // Edge cases
        try {
            BigIntegerMath.log2(new BigInteger("0"), RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Test passed
        }
        
        // Error scenarios
        try {
            BigIntegerMath.log2(null, RoundingMode.DOWN);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testLog10() {
        // Typical use cases
        assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.DOWN));
        
        // Edge cases
        try {
            BigIntegerMath.log10(new BigInteger("0"), RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Test passed
        }
        
        // Error scenarios
        try {
            BigIntegerMath.log10(null, RoundingMode.DOWN);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testSqrt() {
        // Typical use cases
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.DOWN));
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(new BigInteger("25"), RoundingMode.UP));
        
        // Edge cases
        assertEquals(new BigInteger("0"), BigIntegerMath.sqrt(new BigInteger("0"), RoundingMode.DOWN));
        assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.sqrt(new BigInteger("-4"), RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
        try {
            BigIntegerMath.sqrt(null, RoundingMode.DOWN);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testDivide() {
        // Typical use cases
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("2"), RoundingMode.DOWN));
        assertEquals(new BigInteger("4"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.UP));
        
        // Edge cases
        assertEquals(new BigInteger("0"), BigIntegerMath.divide(new BigInteger("0"), new BigInteger("5"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.divide(new BigInteger("10"), new BigInteger("0"), RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Test passed
        }
        try {
            BigIntegerMath.divide(null, new BigInteger("5"), RoundingMode.DOWN);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
    
    @Test
    public void testFactorial() {
        // Typical use cases
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("2432902008176640000"), BigIntegerMath.factorial(20));
        
        // Edge cases
        assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        
        // Error scenarios
        try {
            BigIntegerMath.factorial(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testBinomial() {
        // Typical use cases
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("210"), BigIntegerMath.binomial(10, 6));
        
        // Edge cases
        assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        
        // Error scenarios
        try {
            BigIntegerMath.binomial(5, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
        try {
            BigIntegerMath.binomial(-5, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    @Test
    public void testFitsInLong() {
        // Typical use cases
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("1000000")));
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("10000000000000000000")));
        
        // Edge cases
        assertFalse(BigIntegerMath.fitsInLong(new BigInteger("-9223372036854775809")));
        assertTrue(BigIntegerMath.fitsInLong(new BigInteger("-9223372036854775808")));
        
        // Error scenarios
        try {
            BigIntegerMath.fitsInLong(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
}