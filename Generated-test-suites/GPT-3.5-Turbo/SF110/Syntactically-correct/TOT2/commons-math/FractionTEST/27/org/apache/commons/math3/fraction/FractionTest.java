package org.apache.commons.math3.fraction;
import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-1, 2);
        Fraction absFraction = fraction.abs();
        assertEquals(new Fraction(1, 2), absFraction);
        
        Fraction zeroFraction = new Fraction(0, 1);
        assertEquals(new Fraction(0, 1), zeroFraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals(-1, fraction1.compareTo(fraction2));
        
        Fraction fraction3 = new Fraction(1, 2);
        assertEquals(0, fraction1.compareTo(fraction3));
        
        Fraction fraction4 = new Fraction(5, 8);
        assertEquals(1, fraction2.compareTo(fraction4));
    }

    // Additional test cases for other methods can be added here
    
}