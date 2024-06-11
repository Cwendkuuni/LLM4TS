
import org.junit.Test;
import java.math.BigInteger;

public class BigIntegerMathTest {

    @Test
    public void isPowerOfTwo_returnsTrueForPowerOfTwo() {
        BigInteger powerOfTwo = new BigInteger("4");
        assertTrue(BigIntegerMath.isPowerOfTwo(powerOfTwo));
    }
    
    @Test
    public void isPowerOfTwo_returnsFalseForNonPowerOfTwo() {
        BigInteger nonPowerOfTwo = new BigInteger("5");
        assertFalse(BigIntegerMath.isPowerOfTwo(nonPowerOfTwo));
    }
    
    @Test
    public void isPowerOfTwo_returnsFalseForNegativeNumber() {
        BigInteger negativeNumber = new BigInteger("-2");
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));
    }
    
    @Test
    public void log2_returnsLogBase2OfNumber() {
        BigInteger number = new BigInteger("64");
        int expected = 6;
        assertEquals(expected, BigIntegerMath.log2(number, RoundingMode.UNNECESSARY));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void log2_throwsIllegalArgumentExceptionForZero() {
        BigInteger zero = new BigInteger("0");
        BigIntegerMath.log2(zero, RoundingMode.UNNECESSARY);
    }
    
    @Test
    public void log10_returnsLogBase10OfNumber() {
        BigInteger number = new BigInteger("1000");
        int expected = 3;
        assertEquals(expected, BigIntegerMath.log10(number, RoundingMode.UNNECESSARY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void log10_throwsIllegalArgumentExceptionForZero() {
        BigInteger zero = new BigInteger("0");
        BigIntegerMath.log10(zero, RoundingMode.UNNECESSARY);
    }
    
    @Test
    public void sqrt_returnsSquareRootOfNumber() {
        BigInteger number = new BigInteger("9");
        BigInteger expected = new BigInteger("3");
        assertEquals(expected, BigIntegerMath.sqrt(number, RoundingMode.UNNECESSARY));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void sqrt_throwsIllegalArgumentExceptionForNegativeNumber() {
        BigInteger negativeNumber = new BigInteger("-9");
        BigIntegerMath.sqrt(negativeNumber, RoundingMode.UNNECESSARY);
    }
    
    @Test
    public void divide_returnsQuotientOfTwoNumbers() {
        BigInteger dividend = new BigInteger("10");
        BigInteger divisor = new BigInteger("4");
        BigInteger expected = new BigInteger("2");
        assertEquals(expected, BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY));
    }
    
    @Test(expected = ArithmeticException.class)
    public void divide_throwsArithmeticExceptionForDivisionByZero() {
        BigInteger dividend = new BigInteger("10");
        BigInteger divisor = new BigInteger("0");
        BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
    }
    
    @Test
    public void factorial_returnsFactorialOfNumber() {
        int number = 5;
        BigInteger expected = new BigInteger("120");
        assertEquals(expected, BigIntegerMath.factorial(number));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void factorial_throwsIllegalArgumentExceptionForNegativeNumber() {
        int negativeNumber = -5;
        BigIntegerMath.factorial(negativeNumber);
    }
    
    @Test
    public void binomial_returnsBinomialCoefficientOfTwoNumbers() {
        int n = 5;
        int k = 2;
        BigInteger expected = new BigInteger("10");
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void binomial_throwsIllegalArgumentExceptionForNegativeNumber() {
        int n = -5;
        int k = 2;
        BigIntegerMath.binomial(n, k);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void binomial_throwsIllegalArgumentExceptionForKGreaterThanN() {
        int n = 5;
        int k = 10;
        BigIntegerMath.binomial(n, k);
    }
    
}
