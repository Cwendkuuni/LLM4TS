import org.junit.Assert;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(5, 10);
        Assert.assertEquals(1, fraction.getNumerator());
        Assert.assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 10);
        Fraction absFraction = fraction.abs();
        Assert.assertEquals(5, absFraction.getNumerator());
        Assert.assertEquals(10, absFraction.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Assert.assertEquals(1, fraction1.compareTo(fraction2));
        Assert.assertEquals(-1, fraction2.compareTo(fraction1));
        Assert.assertEquals(0, fraction1.compareTo(fraction1));
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);
        Assert.assertEquals(0.75, fraction.doubleValue(), 0.0001);
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionException1() {
        Fraction fraction = new Fraction(1.5);
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionException2() {
        Fraction fraction = new Fraction(1.5, 0.000001, 100);
    }

    @Test(expected = MathArithmeticException.class)
    public void testMathArithmeticException1() {
        Fraction fraction = new Fraction(5, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testMathArithmeticException2() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, 1);
        fraction.negate();
    }

    // Merge all the individual test cases into a complete JUnit test file
    // 

    @Test
    public void testConstructor() {
        // ...
    }

    @Test
    public void testAbs() {
        // ...
    }

    @Test
    public void testCompareTo() {
        // ...
    }

    @Test
    public void testDoubleValue() {
        // ...
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionException1() {
        // ...
    }

    @Test(expected = FractionConversionException.class)
    public void testFractionConversionException2() {
        // ...
    }

    @Test(expected = MathArithmeticException.class)
    public void testMathArithmeticException1() {
        // ...
    }

    @Test(expected = MathArithmeticException.class)
    public void testMathArithmeticException2() {
        // ...
    }

    // 
}