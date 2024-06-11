import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 3);
        Fraction absFraction = fraction.abs();
        assertEquals(new Fraction(5, 3), absFraction);
    }
    
    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(5, 2);
        double result = fraction.doubleValue();
        assertEquals(2.5, result, 0.0001);
    }
    
    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(-5, 3);
        Fraction fraction2 = new Fraction(-5, 3);
        assertTrue(fraction1.equals(fraction2));
    }
    
    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(15, 7);
        assertEquals(15, fraction.getNumerator());
    }
    
    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(15, 7);
        assertEquals(7, fraction.getDenominator());
    }
    
}