import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UNNECESSARY));
    }
    
    // Additional test cases for log2 to cover edge cases and exceptions
    
    @Test
    public void testLog2Zero() {
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testLog10() {
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log10(new BigInteger("10000"), RoundingMode.UNNECESSARY));
    }
    
    // Additional test cases for log10 to cover edge cases and exceptions
    
    @Test
    public void testLog10Zero() {
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
    
    @Test
    public void testSqrt() {
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.CEILING));
    }
    
    // Additional test cases for sqrt to cover edge cases and exceptions
    
    @Test
    public void testSqrtZero() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
    }
    
    @Test
    public void testDivide() {
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("6"), new BigInteger("3"), RoundingMode.UNNECESSARY));
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("7"), new BigInteger("3"), RoundingMode.FLOOR));
        assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("7"), new BigInteger("2"), RoundingMode.CEILING));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("720"), BigIntegerMath.factorial(6));
    }
    
    // Additional test cases for factorial to cover edge cases and exceptions
    
    @Test
    public void testFactorialNegative() {
        try {
            BigIntegerMath.factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("6"), BigIntegerMath.binomial(4, 2));
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("20"), BigIntegerMath.binomial(6, 3));
    }
    
    // Additional test cases for binomial to cover edge cases and exceptions
    
    @Test
    public void testBinomialNegative() {
        try {
            BigIntegerMath.binomial(-1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}