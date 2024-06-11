import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        BigInteger y = new BigInteger("16");
        assertTrue(BigIntegerMath.isPowerOfTwo(y));

        BigInteger z = new BigInteger("10");
        assertFalse(BigIntegerMath.isPowerOfTwo(z));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        assertEquals(0, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));

        BigInteger y = new BigInteger("16");
        assertEquals(4, BigIntegerMath.log2(y, RoundingMode.UNNECESSARY));

        BigInteger z = new BigInteger("10");
        assertEquals(3, BigIntegerMath.log2(z, RoundingMode.UNNECESSARY));
    }

    // Add test cases for other methods

}