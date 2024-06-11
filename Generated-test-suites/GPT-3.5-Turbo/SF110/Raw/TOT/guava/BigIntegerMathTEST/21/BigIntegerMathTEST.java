import org.junit.Test;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_withPowerOfTwoNumber_returnTrue() {
        BigInteger x = BigInteger.valueOf(8);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_withNonPowerOfTwoNumber_returnFalse() {
        BigInteger x = BigInteger.valueOf(9);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testIsPowerOfTwo_withNegativeNumber_returnFalse() {
        BigInteger x = BigInteger.valueOf(-4);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2_withPositiveNumberAndModeFloor_returnFloorValue() {
        BigInteger x = BigInteger.valueOf(16);
        int result = BigIntegerMath.log2(x, RoundingMode.FLOOR);
        assertEquals(4, result);
    }

    @Test
    public void testLog2_withNegativeNumber_throwException() {
        BigInteger x = BigInteger.valueOf(-9);
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(x, RoundingMode.FLOOR));
    }

    @Test
    public void testLog10_withPositiveNumberAndModeCeiling_returnCeilingValue() {
        BigInteger x = BigInteger.valueOf(100);
        int result = BigIntegerMath.log10(x, RoundingMode.CEILING);
        assertEquals(3, result);
    }

    @Test
    public void testLog10_withNegativeNumber_throwException() {
        BigInteger x = BigInteger.valueOf(-25);
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log10(x, RoundingMode.CEILING));
    }

    @Test
    public void testSqrt_withPerfectSquareAndModeFloor_returnFloorValue() {
        BigInteger x = BigInteger.valueOf(121);
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.FLOOR);
        assertEquals(BigInteger.valueOf(11), result);
    }

    @Test
    public void testSqrt_withNegativeNumber_throwException() {
        BigInteger x = BigInteger.valueOf(-9);
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.sqrt(x, RoundingMode.FLOOR));
    }

    @Test
    public void testDivide_withPositiveDividendAndNonZeroPositiveDivisor_returnQuotient() {
        BigInteger p = BigInteger.valueOf(25);
        BigInteger q = BigInteger.valueOf(5);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.HALF_EVEN);
        assertEquals(BigInteger.valueOf(5), result);
    }

    @Test
    public void testDivide_withNegativeDividendAndNonZeroPositiveDivisor_returnQuotient() {
        BigInteger p = BigInteger.valueOf(-25);
        BigInteger q = BigInteger.valueOf(5);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.HALF_EVEN);
        assertEquals(BigInteger.valueOf(-5), result);
    }

    @Test
    public void testFactorial_withZero_returnOne() {
        BigInteger result = BigIntegerMath.factorial(0);
        assertEquals(BigInteger.ONE, result);
    }

    @Test
    public void testFactorial_withPositiveNumber_returnCorrectResult() {
        BigInteger result = BigIntegerMath.factorial(5);
        assertEquals(BigInteger.valueOf(120), result);
    }

    @Test
    public void testFactorial_withNegativeNumber_throwException() {
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.factorial(-5));
    }

    @Test
    public void testBinomial_withZeroAndZero_returnOne() {
        BigInteger result = BigIntegerMath.binomial(0, 0);
        assertEquals(BigInteger.ONE, result);
    }

    @Test
    public void testBinomial_withPositiveNumbers_returnCorrectResult() {
        BigInteger result = BigIntegerMath.binomial(5, 2);
        assertEquals(BigInteger.valueOf(10), result);
    }

    @Test
    public void testBinomial_withNegativeNumber_throwException() {
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(5, -2));
    }

    @Test
    public void testFitsInLong_withNumberSmallerThanLong_returnTrue() {
        BigInteger x = BigInteger.valueOf(100);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testFitsInLong_withNumberEqualToLong_returnTrue() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE);
        assertTrue(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testFitsInLong_withNumberGreaterThanLong_returnFalse() {
        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE);
        assertFalse(BigIntegerMath.fitsInLong(x));
    }

    @Test
    public void testListProduct_withEmptyList_returnOne() {
        List<BigInteger> nums = new ArrayList<>();
        BigInteger result = BigIntegerMath.listProduct(nums);
        assertEquals(BigInteger.ONE, result);
    }

    @Test
    public void testListProduct_withNumbersInList_returnCorrectResult() {
        List<BigInteger> nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(2));
        nums.add(BigInteger.valueOf(3));
        nums.add(BigInteger.valueOf(4));
        BigInteger result = BigIntegerMath.listProduct(nums);
        assertEquals(BigInteger.valueOf(24), result);
    }

    @Test
    public void testListProduct_withNumbersInRange_returnCorrectResult() {
        List<BigInteger> nums = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            nums.add(BigInteger.valueOf(i));
        }
        BigInteger result = BigIntegerMath.listProduct(nums);
        assertEquals(new BigInteger("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000"), result);
    }

}