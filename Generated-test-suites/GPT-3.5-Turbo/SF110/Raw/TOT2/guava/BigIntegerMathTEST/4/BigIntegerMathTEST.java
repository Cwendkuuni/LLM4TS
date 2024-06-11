import static org.junit.Assert.*;
import org.junit.Test;
import java.math.*;
import java.math.RoundingMode;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = new BigInteger("8");
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = new BigInteger("12");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.FLOOR));

        x = new BigInteger("30");
        assertEquals(4, BigIntegerMath.log2(x, RoundingMode.FLOOR));
    }

    @Test
    public void testLog10() {
        BigInteger x = new BigInteger("100");
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.FLOOR));

        x = new BigInteger("500");
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.CEILING));
    }

    @Test
    public void testSqrt() {
        BigInteger x = new BigInteger("25");
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(x, RoundingMode.FLOOR));

        x = new BigInteger("30");
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(x, RoundingMode.FLOOR));
    }

    @Test
    public void testDivide() {
        BigInteger p = new BigInteger("10");
        BigInteger q = new BigInteger("5");
        assertEquals(new BigInteger("2"), BigIntegerMath.divide(p, q, RoundingMode.FLOOR));

        p = new BigInteger("15");
        q = new BigInteger("3");
        assertEquals(new BigInteger("5"), BigIntegerMath.divide(p, q, RoundingMode.FLOOR));
    }

    @Test
    public void testFactorial() {
        assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        assertEquals(new BigInteger("5040"), BigIntegerMath.factorial(7));
    }

    @Test
    public void testBinomial() {
        assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        assertEquals(new BigInteger("252"), BigIntegerMath.binomial(10, 5));
    }

}