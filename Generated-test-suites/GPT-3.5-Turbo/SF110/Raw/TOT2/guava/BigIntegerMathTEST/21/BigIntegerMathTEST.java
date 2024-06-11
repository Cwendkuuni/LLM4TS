import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Typical use case
        // Test with positive power of two
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        
        // Edge case
        // Test with zero
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        
        // Error scenario
        // Test with negative number
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-8)));
    }
    
    @Test
    public void testLog2() {
        // Typical use case
        // Test with power of two
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
        
        // Edge case
        // Test with one
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        
        // Error scenario
        // Test with a negative number
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(BigInteger.valueOf(-8), RoundingMode.UNNECESSARY));
    }
    
    // Repeat the same pattern for remaining methods...

}