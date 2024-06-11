import static org.junit.Assert.*;
import org.junit.Test;
import java.math.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    // Test isPowerOfTwo with positive power of 2 numbers
    @Test
    public void testIsPowerOfTwo_PositivePowerOfTwo() {
        BigInteger x = BigInteger.valueOf(2);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(1024);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }
    
    // Test isPowerOfTwo with non-power of 2 numbers
    @Test
    public void testIsPowerOfTwo_NonPowerOfTwo() {
        BigInteger x = BigInteger.valueOf(3);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(15);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(1000);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    // Test isPowerOfTwo with negative numbers
    @Test
    public void testIsPowerOfTwo_NegativeNumbers() {
        BigInteger x = BigInteger.valueOf(-2);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(-16);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        
        x = BigInteger.valueOf(-1024);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
    
    // TODO: Add more tests for isPowerOfTwo
    
    // Test log2 with positive numbers
    @Test
    public void testLog2_PositiveNumbers() {
        BigInteger x = BigInteger.valueOf(2);
        int expected = 1;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
        
        x = BigInteger.valueOf(10);
        expected = 3;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));
        
        x = BigInteger.valueOf(1024);
        expected = 10;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));
    }
    
    // Test log2 with negative numbers
    @Test (expected = IllegalArgumentException.class)
    public void testLog2_NegativeNumbers() {
        BigInteger x = BigInteger.valueOf(-2);
        BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
    }
    
    // Test log2 with zero
    @Test (expected = IllegalArgumentException.class)
    public void testLog2_Zero() {
        BigInteger x = BigInteger.ZERO;
        BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
    }
    
    // TODO: Add more tests for log2
    
    // Test log10 with positive numbers
    @Test
    public void testLog10_PositiveNumbers() {
        BigInteger x = BigInteger.valueOf(10);
        int expected = 1;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));
        
        x = BigInteger.valueOf(100);
        expected = 2;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN));
        
        x = BigInteger.valueOf(10000);
        expected = 4;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN));
    }
    
    // Test log10 with negative numbers
    @Test (expected = IllegalArgumentException.class)
    public void testLog10_NegativeNumbers() {
        BigInteger x = BigInteger.valueOf(-10);
        BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
    }
    
    // Test log10 with zero
    @Test (expected = IllegalArgumentException.class)
    public void testLog10_Zero() {
        BigInteger x = BigInteger.ZERO;
        BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
    }
    
    // TODO: Add more tests for log10
    
    // Test sqrt with perfect square numbers
    @Test
    public void testSqrt_PerfectSquare() {
        BigInteger x = BigInteger.valueOf(4);
        BigInteger expected = BigInteger.valueOf(2);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
        
        x = BigInteger.valueOf(16);
        expected = BigInteger.valueOf(4);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
        
        x = BigInteger.valueOf(100);
        expected = BigInteger.valueOf(10);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }
    
    // Test sqrt with non-perfect square numbers
    @Test
    public void testSqrt_NonPerfectSquare() {
        BigInteger x = BigInteger.valueOf(2);
        BigInteger expected = BigInteger.valueOf(1);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
        
        x = BigInteger.valueOf(10);
        expected = BigInteger.valueOf(3);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
        
        x = BigInteger.valueOf(101);
        expected = BigInteger.valueOf(10);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }

    // TODO: Add more tests for sqrt

    // Test divide
    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger expected = BigInteger.valueOf(5);
        assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }

    // TODO: Add more tests for divide

    // Test factorial
    @Test
    public void testFactorial() {
        int n = 5;
        BigInteger expected = BigInteger.valueOf(120);
        assertEquals(expected, BigIntegerMath.factorial(n));
    }

    // TODO: Add more tests for factorial

    // Test binomial
    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;
        BigInteger expected = BigInteger.valueOf(10);
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }

    // TODO: Add more tests for binomial

}