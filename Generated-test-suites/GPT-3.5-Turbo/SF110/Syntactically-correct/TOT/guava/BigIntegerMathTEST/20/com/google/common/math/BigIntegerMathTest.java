import org.junit.Test;
import java.math.BigInteger;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo_typical() {
        BigInteger x = BigInteger.valueOf(8);
        assert(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_edge() {
        BigInteger x = BigInteger.valueOf(1);
        assert(BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testIsPowerOfTwo_error() {
        BigInteger x = BigInteger.valueOf(-8);
        assert(!BigIntegerMath.isPowerOfTwo(x));
    }
    
    @Test
    public void testLog2_typical() {
        BigInteger x = BigInteger.valueOf(16);
        assert(BigIntegerMath.log2(x, RoundingMode.UNNECESSARY) == 4);
    }
    
    @Test
    public void testLog2_edge() {
        BigInteger x = BigInteger.valueOf(128);
        assert(BigIntegerMath.log2(x, RoundingMode.UNNECESSARY) == 7);
    }
    
    @Test
    public void testLog2_error() {
        BigInteger x = BigInteger.valueOf(-16);
        try {
            BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }
    
    @Test
    public void testLog10_typical() {
        BigInteger x = BigInteger.valueOf(100);
        assert(BigIntegerMath.log10(x, RoundingMode.UNNECESSARY) == 2);
    }
    
    @Test
    public void testLog10_edge() {
        BigInteger x = BigInteger.valueOf(1000);
        assert(BigIntegerMath.log10(x, RoundingMode.UNNECESSARY) == 3);
    }
    
    @Test
    public void testLog10_error() {
        BigInteger x = BigInteger.valueOf(-100);
        try {
            BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }
    
    @Test
    public void testSqrt_typical() {
        BigInteger x = BigInteger.valueOf(25);
        assert(BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY) == 5);
    }
    
    @Test
    public void testSqrt_edge() {
        BigInteger x = BigInteger.valueOf(121);
        assert(BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY) == 11);
    }
    
    @Test
    public void testSqrt_error() {
        BigInteger x = BigInteger.valueOf(-25);
        try {
            BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }
    
    @Test
    public void testDivide_typical() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        assert(BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY) == 5);
    }
    
    @Test
    public void testDivide_edge() {
        BigInteger p = BigInteger.valueOf(100);
        BigInteger q = BigInteger.valueOf(25);
        assert(BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY) == 4);
    }
    
    @Test
    public void testDivide_error() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(0);
        try {
            BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
            assert(false);
        } catch (ArithmeticException e) {
            assert(true);
        }
    }
    
    @Test
    public void testFactorial_typical() {
        assert(BigIntegerMath.factorial(5).equals(BigInteger.valueOf(120)));
    }
    
    @Test
    public void testFactorial_error() {
        try {
            BigIntegerMath.factorial(-5);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }
    
    @Test
    public void testBinomial_typical() {
        assert(BigIntegerMath.binomial(5, 3).equals(BigInteger.valueOf(10)));
    }
    
    @Test
    public void testBinomial_error() {
        try {
            BigIntegerMath.binomial(5, 6);
            assert(false);
        } catch (IllegalArgumentException e) {
            assert(true);
        }
    }
}