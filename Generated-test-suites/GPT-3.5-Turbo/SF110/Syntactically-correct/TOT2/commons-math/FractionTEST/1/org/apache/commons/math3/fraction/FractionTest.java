package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testConstructor1() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testConstructor2() {
        Fraction fraction = new Fraction(5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction absFraction = fraction.abs();
        assertEquals(3, absFraction.getNumerator());
        assertEquals(4, absFraction.getDenominator());
    }

    @Test
    public void testCompareTo_LessThan() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 3);
        assertTrue(fraction1.compareTo(fraction2) < 0);
    }

    @Test
    public void testCompareTo_GreaterThan() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue(fraction1.compareTo(fraction2) > 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(5, 2);
        assertEquals(2.5, fraction.doubleValue(), 0.0001);
    }

    // Add more test cases for other methods as needed

}