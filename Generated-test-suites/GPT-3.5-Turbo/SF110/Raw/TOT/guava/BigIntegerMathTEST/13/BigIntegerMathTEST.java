import org.junit.Test;
import org.junit.Assert;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    // Test isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        // Typical use cases
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("4")));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1024")));
        
        // Edge cases
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("2")));
        Assert.assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
        
        // Error scenarios
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("9")));
        Assert.assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-4")));
    }

    // Test log2 method
    @Test
    public void testLog2() {
        // Typical use cases
        Assert.assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.FLOOR));
        Assert.assertEquals(1, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.FLOOR));
        Assert.assertEquals(3, BigIntegerMath.log2(new BigInteger("10"), RoundingMode.FLOOR));
        
        // Edge cases
        Assert.assertEquals(10, BigIntegerMath.log2(new BigInteger("1024"), RoundingMode.FLOOR));
        Assert.assertEquals(10, BigIntegerMath.log2(new BigInteger("1025"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.log2(new BigInteger("-1"), RoundingMode.FLOOR);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test log10 method
    @Test
    public void testLog10() {
        // Typical use cases
        Assert.assertEquals(0, BigIntegerMath.log10(new BigInteger("1"), RoundingMode.FLOOR));
        Assert.assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.FLOOR));
        Assert.assertEquals(1, BigIntegerMath.log10(new BigInteger("11"), RoundingMode.CEILING));
        Assert.assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.FLOOR));
        
        // Edge cases
        Assert.assertEquals(10, BigIntegerMath.log10(new BigInteger("10000000000"), RoundingMode.FLOOR));
        Assert.assertEquals(10, BigIntegerMath.log10(new BigInteger("10000000001"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.log10(new BigInteger("-1"), RoundingMode.FLOOR);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test sqrt method
    @Test
    public void testSqrt() {
        // Typical use cases
        Assert.assertEquals(new BigInteger("1"), BigIntegerMath.sqrt(new BigInteger("1"), RoundingMode.FLOOR));
        Assert.assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("10"), RoundingMode.FLOOR));
        Assert.assertEquals(new BigInteger("4"), BigIntegerMath.sqrt(new BigInteger("16"), RoundingMode.CEILING));
        Assert.assertEquals(new BigInteger("10"), BigIntegerMath.sqrt(new BigInteger("100"), RoundingMode.FLOOR));
        
        // Edge cases
        Assert.assertEquals(new BigInteger("1024"), BigIntegerMath.sqrt(new BigInteger("1048576"), RoundingMode.FLOOR));
        Assert.assertEquals(new BigInteger("1024"), BigIntegerMath.sqrt(new BigInteger("1048577"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.sqrt(new BigInteger("-1"), RoundingMode.FLOOR);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test divide method
    @Test
    public void testDivide() {
        // Typical use cases
        Assert.assertEquals(new BigInteger("2"), BigIntegerMath.divide(new BigInteger("4"), new BigInteger("2"), RoundingMode.UNNECESSARY));
        Assert.assertEquals(new BigInteger("3"), BigIntegerMath.divide(new BigInteger("10"), new BigInteger("3"), RoundingMode.FLOOR));
        Assert.assertEquals(new BigInteger("5"), BigIntegerMath.divide(new BigInteger("25"), new BigInteger("5"), RoundingMode.CEILING));
        Assert.assertEquals(new BigInteger("9"), BigIntegerMath.divide(new BigInteger("100"), new BigInteger("10"), RoundingMode.HALF_UP));
        
        // Edge cases
        Assert.assertEquals(new BigInteger("524288"), BigIntegerMath.divide(new BigInteger("1048576"), new BigInteger("2"), RoundingMode.FLOOR));
        Assert.assertEquals(new BigInteger("524288"), BigIntegerMath.divide(new BigInteger("1048575"), new BigInteger("2"), RoundingMode.CEILING));
        
        // Error scenarios
        try {
            BigIntegerMath.divide(new BigInteger("4"), new BigInteger("0"), RoundingMode.UNNECESSARY);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test factorial method
    @Test
    public void testFactorial() {
        // Typical use cases
        Assert.assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
        Assert.assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
        Assert.assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));
        Assert.assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(10));
        
        // Edge cases
        Assert.assertEquals(new BigInteger("6227020800"), BigIntegerMath.factorial(13));
        Assert.assertEquals(new BigInteger("7054329737563289920"), BigIntegerMath.factorial(19));
        
        // Error scenarios
        try {
            BigIntegerMath.factorial(-1);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test binomial method
    @Test
    public void testBinomial() {
        // Typical use cases
        Assert.assertEquals(new BigInteger("1"), BigIntegerMath.binomial(5, 0));
        Assert.assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 1));
        Assert.assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 2));
        Assert.assertEquals(new BigInteger("10"), BigIntegerMath.binomial(5, 3));
        
        // Edge cases
        Assert.assertEquals(new BigInteger("5"), BigIntegerMath.binomial(5, 5));
        Assert.assertEquals(new BigInteger("19"), BigIntegerMath.binomial(19, 1));
        
        // Error scenarios
        try {
            BigIntegerMath.binomial(5, -1);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
        try {
            BigIntegerMath.binomial(5, 6);
            Assert.fail("Exception not thrown");
        } catch(IllegalArgumentException e) {
            // Expected exception
        }
    }
}