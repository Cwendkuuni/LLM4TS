
import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;

public class FractionTest {
    @Test
    public void testAbs() {
        // Test case 1: Positive numerator
        Fraction fraction1 = new Fraction(3, 4);
        Fraction result1 = fraction1.abs();
        Assert.assertEquals(3, result1.getNumerator());
        Assert.assertEquals(4, result1.getDenominator());
        
        // Test case 2: Negative numerator
        Fraction fraction2 = new Fraction(-5, 6);
        Fraction result2 = fraction2.abs();
        Assert.assertEquals(5, result2.getNumerator());
        Assert.assertEquals(6, result2.getDenominator());
    }
    
    @Test
    public void testCompareTo() {
        // Test case 1: fraction1 < fraction2
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        int result1 = fraction1.compareTo(fraction2);
        Assert.assertTrue(result1 < 0);
        
        // Test case 2: fraction1 > fraction2
        Fraction fraction3 = new Fraction(5, 8);
        Fraction fraction4 = new Fraction(2, 3);
        int result2 = fraction3.compareTo(fraction4);
        Assert.assertTrue(result2 > 0);
        
        // Test case 3: fraction1 = fraction2
        Fraction fraction5 = new Fraction(3, 5);
        Fraction fraction6 = new Fraction(3, 5);
        int result3 = fraction5.compareTo(fraction6);
        Assert.assertTrue(result3 == 0);
    }
    
    @Test
    public void testDoubleValue() {
        // Test case 1: Positive fraction
        Fraction fraction1 = new Fraction(7, 8);
        double result1 = fraction1.doubleValue();
        Assert.assertEquals(0.875, result1, 0.0);
        
        // Test case 2: Negative fraction
        Fraction fraction2 = new Fraction(-2, 3);
        double result2 = fraction2.doubleValue();
        Assert.assertEquals(-0.6666666666666666, result2, 0.0);
    }
    
    // ... Other test cases for each public method ...
    
}

