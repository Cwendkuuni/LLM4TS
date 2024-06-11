import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FractionTest {
    
    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(3, 4);
    }
    
    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        fraction = new Fraction(2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }
    
    @Test
    public void testConstructorException() {
        try {
            Fraction fraction = new Fraction(5, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("Zero denominator in fraction: 5 / 0", e.getMessage());
        }
    }
    
    @Test
    public void testAdd() {
        Fraction result = fraction.add(new Fraction(1, 2));
        assertEquals(5, result.getNumerator());
        assertEquals(4, result.getDenominator());
        
        result = fraction.add(new Fraction(0));
        assertEquals(fraction.getNumerator(), result.getNumerator());
        assertEquals(fraction.getDenominator(), result.getDenominator());
    }
    
    @Test
    public void testSubtract() {
        Fraction result = fraction.subtract(new Fraction(1, 2));
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
        
        result = fraction.subtract(new Fraction(0));
        assertEquals(fraction.getNumerator(), result.getNumerator());
        assertEquals(fraction.getDenominator(), result.getDenominator());
    }
    
    @Test
    public void testMultiply() {
        Fraction result = fraction.multiply(new Fraction(1, 2));
        assertEquals(3, result.getNumerator());
        assertEquals(8, result.getDenominator());
        
        result = fraction.multiply(new Fraction(0));
        assertEquals(0, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }
    
    @Test
    public void testDivide() {
        Fraction result = fraction.divide(new Fraction(1, 2));
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
        
        try {
            result = fraction.divide(new Fraction(0));
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            assertEquals("Zero fraction cannot be divided by", e.getMessage());
        }
    }

    @Test
    public void testDoubleValue() {
        double result = fraction.doubleValue();
        assertEquals(0.75, result, 0.0001);
    }
    
    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);
        
        assertTrue(fraction.equals(fraction1));
        assertTrue(fraction.equals(fraction2));
        assertFalse(fraction.equals(new Fraction(1, 2)));
        assertFalse(fraction.equals(null));
        assertFalse(fraction.equals(new String("3/4")));
    }
    
    @Test
    public void testToString() {
        String result = fraction.toString();
        assertEquals("3/4", result);
        
        result = Fraction.ZERO.toString();
        assertEquals("0", result);
    }
    
}