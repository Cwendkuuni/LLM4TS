package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructorWithDouble() {
        Fraction fraction = new Fraction(0.75);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testConstructorWithMaxDenominator() {
        Fraction fraction = new Fraction(0.333, 3);
        assertEquals(1, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2, 3);
        Fraction absFraction = fraction.abs();
        assertEquals(2, absFraction.getNumerator());
        assertEquals(3, absFraction.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 5);
        Fraction fraction2 = new Fraction(2, 4);
        assertEquals(1, fraction1.compareTo(fraction2));
    }

    // Additional test methods for other public methods in Fraction class

}