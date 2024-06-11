import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x1 = new BigInteger("0");
        assert(BigIntegerMath.isPowerOfTwo(x1) == false);
        
        BigInteger x2 = new BigInteger("2");
        assert(BigIntegerMath.isPowerOfTwo(x2) == true);
        
        BigInteger x3 = new BigInteger("10");
        assert(BigIntegerMath.isPowerOfTwo(x3) == false);
    }
    
    @Test
    public void testLog2() {
        BigInteger x1 = new BigInteger("0");
        assert(BigIntegerMath.log2(x1, RoundingMode.DOWN) == 0);
        
        BigInteger x2 = new BigInteger("16");
        assert(BigIntegerMath.log2(x2, RoundingMode.DOWN) == 4);
        
        BigInteger x3 = new BigInteger("10");
        assert(BigIntegerMath.log2(x3, RoundingMode.DOWN) == 3);
    }
    
    @Test
    public void testLog10() {
        BigInteger x1 = new BigInteger("0");
        assert(BigIntegerMath.log10(x1, RoundingMode.DOWN) == 0);
        
        BigInteger x2 = new BigInteger("100");
        assert(BigIntegerMath.log10(x2, RoundingMode.DOWN) == 2);
        
        BigInteger x3 = new BigInteger("10");
        assert(BigIntegerMath.log10(x3, RoundingMode.DOWN) == 1);
    }
    
    @Test
    public void testSqrt() {
        BigInteger x1 = new BigInteger("0");
        assert(BigIntegerMath.sqrt(x1, RoundingMode.DOWN) == new BigInteger("0"));
        
        BigInteger x2 = new BigInteger("16");
        assert(BigIntegerMath.sqrt(x2, RoundingMode.DOWN) == new BigInteger("4"));
        
        BigInteger x3 = new BigInteger("10");
        assert(BigIntegerMath.sqrt(x3, RoundingMode.DOWN) == new BigInteger("3"));
    }
    
    @Test
    public void testDivide() {
        BigInteger p1 = new BigInteger("10");
        BigInteger q1 = new BigInteger("2");
        assert(BigIntegerMath.divide(p1, q1, RoundingMode.DOWN) == new BigInteger("5"));
        
        BigInteger p2 = new BigInteger("15");
        BigInteger q2 = new BigInteger("3");
        assert(BigIntegerMath.divide(p2, q2, RoundingMode.DOWN) == new BigInteger("5"));
        
        BigInteger p3 = new BigInteger("10");
        BigInteger q3 = new BigInteger("3");
        assert(BigIntegerMath.divide(p3, q3, RoundingMode.DOWN) == new BigInteger("3"));
    }
    
    @Test
    public void testFactorial() {
        assert(BigIntegerMath.factorial(0) == new BigInteger("1"));
        assert(BigIntegerMath.factorial(1) == new BigInteger("1"));
        assert(BigIntegerMath.factorial(5) == new BigInteger("120"));
    }
    
    @Test
    public void testBinomial() {
        assert(BigIntegerMath.binomial(5, 2) == new BigInteger("10"));
        assert(BigIntegerMath.binomial(10, 0) == new BigInteger("1"));
        assert(BigIntegerMath.binomial(10, 5) == new BigInteger("252"));
    }

}