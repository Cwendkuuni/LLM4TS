package org.apache.commons.math3.fraction;
import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {
    
    @Test
    public void testFractionConstructorWithDoubleValue() {
        Fraction fraction = new Fraction(3.5);
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorWithMaxDenominator() {
        Fraction fraction = new Fraction(3.5, 100);
        assertEquals(7, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 3);
        Fraction absFraction = fraction.abs();
        assertEquals(5, absFraction.getNumerator());
        assertEquals(3, absFraction.getDenominator());
    }
    
    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(5, 6);
        assertTrue(fraction1.compareTo(fraction2) < 0);
    }
    
    // Add more test cases for other methods

}