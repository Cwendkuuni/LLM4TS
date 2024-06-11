import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }
    
    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
    }
    
    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
    }
    
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
    }
    
    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TWO, BigInteger.TWO, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.TEN, BigInteger.valueOf(5), RoundingMode.DOWN));
    }
    
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
    }
    
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
    }
}