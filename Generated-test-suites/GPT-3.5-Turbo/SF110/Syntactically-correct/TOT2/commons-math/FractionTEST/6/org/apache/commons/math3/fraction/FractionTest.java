package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {
    
    @Test
    public void testFractionConstructorDoubleValue() {
        Fraction fraction = new Fraction(1.5);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorMaxDenominator() {
        Fraction fraction = new Fraction(1.5, 100);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testFractionConstructorIntValue() {
        Fraction fraction = new Fraction(3);
        assertEquals(3, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }
    
    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        assertEquals(3, fraction.abs().getNumerator());
        assertEquals(4, fraction.abs().getDenominator());
    }
    
    // Add more test cases for other methods in the Fraction class
    
}