
import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_true() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(64)));
    }

    @Test
    public void testIsPowerOfTwo_false() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(7)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(50)));
    }

    @Test
    public void testLog2_floor() {
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.FLOOR));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.FLOOR));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.FLOOR));
    }

    @Test
    public void testLog2_ceiling() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.CEILING));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.CEILING));
    }

    // Add more test cases for remaining methods...

}

