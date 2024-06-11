import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_typicalCase() {
        // Arrange
        BigInteger powerOfTwo = BigInteger.valueOf(16);
        
        // Act
        boolean result = BigIntegerMath.isPowerOfTwo(powerOfTwo);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsPowerOfTwo_notPowerOfTwo() {
        // Arrange
        BigInteger notPowerOfTwo = BigInteger.valueOf(15);
        
        // Act
        boolean result = BigIntegerMath.isPowerOfTwo(notPowerOfTwo);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLog2_modeUnnecessary_powerOfTwo() {
        // Arrange
        BigInteger powerOfTwo = BigInteger.valueOf(16);
        RoundingMode mode = RoundingMode.UNNECESSARY;
        
        // Act
        int result = BigIntegerMath.log2(powerOfTwo, mode);
        
        // Assert
        Assert.assertEquals(4, result);
    }
    
    @Test
    public void testLog2_modeDown_floor() {
        // Arrange
        BigInteger number = BigInteger.valueOf(15);
        RoundingMode mode = RoundingMode.DOWN;
        
        // Act
        int result = BigIntegerMath.log2(number, mode);
        
        // Assert
        Assert.assertEquals(3, result);
    }
    
    @Test
    public void testLog2_modeUp_powerOfTwo_ceil() {
        // Arrange
        BigInteger powerOfTwo = BigInteger.valueOf(16);
        RoundingMode mode = RoundingMode.UP;
        
        // Act
        int result = BigIntegerMath.log2(powerOfTwo, mode);
        
        // Assert
        Assert.assertEquals(4, result);
    }
    
    @Test
    public void testLog10_modeHalfUp_approximation() {
        // Arrange
        BigInteger number = BigInteger.valueOf(300);
        RoundingMode mode = RoundingMode.HALF_UP;
        
        // Act
        int result = BigIntegerMath.log10(number, mode);
        
        // Assert
        Assert.assertEquals(2, result);
    }
    
    @Test
    public void testSqrt_modeUnnecessary_perfectSquare() {
        // Arrange
        BigInteger perfectSquare = BigInteger.valueOf(16);
        RoundingMode mode = RoundingMode.UNNECESSARY;
        
        // Act
        BigInteger result = BigIntegerMath.sqrt(perfectSquare, mode);
        
        // Assert
        Assert.assertEquals(BigInteger.valueOf(4), result);
    }
    
    @Test
    public void testSqrt_modeFloor_notPerfectSquare() {
        // Arrange
        BigInteger number = BigInteger.valueOf(15);
        RoundingMode mode = RoundingMode.FLOOR;
        
        // Act
        BigInteger result = BigIntegerMath.sqrt(number, mode);
        
        // Assert
        Assert.assertEquals(BigInteger.valueOf(3), result);
    }
    
    @Test
    public void testDivide_typicalCase() {
        // Arrange
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.valueOf(2);
        RoundingMode mode = RoundingMode.DOWN;
        
        // Act
        BigInteger result = BigIntegerMath.divide(dividend, divisor, mode);
        
        // Assert
        Assert.assertEquals(BigInteger.valueOf(5), result);
    }
    
    @Test
    public void testFactorial_typicalCase() {
        // Arrange
        int n = 5;
        
        // Act
        BigInteger result = BigIntegerMath.factorial(n);
        
        // Assert
        Assert.assertEquals(BigInteger.valueOf(120), result);
    }
    
    @Test
    public void testBinomial_typicalCase() {
        // Arrange
        int n = 5;
        int k = 2;
        
        // Act
        BigInteger result = BigIntegerMath.binomial(n, k);
        
        // Assert
        Assert.assertEquals(BigInteger.valueOf(10), result);
    }
}