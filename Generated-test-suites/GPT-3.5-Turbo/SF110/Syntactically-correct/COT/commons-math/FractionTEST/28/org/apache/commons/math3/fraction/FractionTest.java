package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-5, 2);
        Fraction absFraction = fraction.abs();
        assertEquals(5, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(2, 4);

        assertTrue(fraction1.compareTo(fraction1) == 0);
        assertTrue(fraction1.compareTo(fraction2) < 0);
        assertTrue(fraction2.compareTo(fraction1) > 0);
        assertTrue(fraction1.compareTo(fraction3) == 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 5);
        double value = fraction.doubleValue();
        assertEquals(0.6, value, 0.00001);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 6);
        Fraction fraction3 = new Fraction(1, 2);

        assertTrue(fraction1.equals(fraction1));
        assertTrue(fraction1.equals(fraction2));
        assertFalse(fraction1.equals(fraction3));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 4);
        float value = fraction.floatValue();
        assertEquals(0.25, value, 0.00001);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 7);
        int denominator = fraction.getDenominator();
        assertEquals(7, denominator);
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(5, 8);
        int numerator = fraction.getNumerator();
        assertEquals(5, numerator);
    }

    // continue with the remaining methods...

}