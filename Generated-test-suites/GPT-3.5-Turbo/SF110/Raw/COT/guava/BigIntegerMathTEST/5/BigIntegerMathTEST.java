import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    // Test isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1024)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(100)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(0)));
    }

    // Test log2 method
    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.CEILING));
        assertEquals(9, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.FLOOR));
        assertEquals(11, BigIntegerMath.log2(BigInteger.valueOf(1536), RoundingMode.UP));
        assertEquals(11, BigIntegerMath.log2(BigInteger.valueOf(1536), RoundingMode.CEILING));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.DOWN));
        assertEquals(10, BigIntegerMath.log2(BigInteger.valueOf(1024), RoundingMode.FLOOR));
    }

    // Test log10 method
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.valueOf(1), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.FLOOR));
    }

    // Test sqrt method
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(10), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.FLOOR));
    }

    // Test divide method
    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(50), BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(7), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(7), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(14), BigInteger.valueOf(7), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(14), BigInteger.valueOf(7), RoundingMode.FLOOR));
    }

    // Test factorial method
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("1307674368000"), BigIntegerMath.factorial(15));
        assertEquals(new BigInteger("933262154439441526816992388562667004907159682643816214685929638952175999932299156089414639761565182862536979208272237582511852109168640000000000000000000000"), BigIntegerMath.factorial(100));
    }
    
    // Test binomial method
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(10, 3));
        assertEquals(BigInteger.valueOf(286), BigIntegerMath.binomial(13, 5));
        assertEquals(BigInteger.valueOf(1365), BigIntegerMath.binomial(30, 6));
    }

    // TODO: Test additional edge cases and exception handling scenarios

}

