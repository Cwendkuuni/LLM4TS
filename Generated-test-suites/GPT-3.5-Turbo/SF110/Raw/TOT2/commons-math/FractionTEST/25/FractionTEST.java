import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testFractionConstructorWithDoubleValue() {
        Fraction fraction = new Fraction(5.5);
        assertEquals("Fraction numerator should be 11", 11, fraction.getNumerator());
        assertEquals("Fraction denominator should be 2", 2, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorWithMaxIterations() {
        try {
            Fraction fraction = new Fraction(3.7, 0.1, 5);
            fail("FractionConversionException should have been thrown");
        } catch (FractionConversionException e) {
            // Exception expected, test passes
        }
    }
    
    @Test
    public void testFractionConstructorWithZeroDenominator() {
        try {
            Fraction fraction = new Fraction(2, 0);
            fail("MathArithmeticException should have been thrown for zero denominator");
        } catch (MathArithmeticException e) {
            // Exception expected, test passes
        }
    }
    
    @Test
    public void testFractionAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction absFraction = fraction.abs();
        assertEquals("Absolute value of fraction should have positive numerator", 3, absFraction.getNumerator());
    }
    
    // Additional test cases for other methods can be added here
    
}