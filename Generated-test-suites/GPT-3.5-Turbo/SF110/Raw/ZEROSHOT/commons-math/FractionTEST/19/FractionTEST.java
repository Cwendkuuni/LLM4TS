package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(1, 2);
    }

    @Test
    public void testAbs() {
        Fraction absFraction = fraction.abs();
        assertEquals(1, absFraction.getNumerator());
        assertEquals(2, absFraction.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fractionToAdd = new Fraction(1, 3);
        Fraction result = fraction.add(fractionToAdd);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fractionToSubtract = new Fraction(1, 4);
        Fraction result = fraction.subtract(fractionToSubtract);
        assertEquals(1, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fractionToMultiply = new Fraction(2, 3);
        Fraction result = fraction.multiply(fractionToMultiply);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fractionToDivide = new Fraction(2, 5);
        Fraction result = fraction.divide(fractionToDivide);
        assertEquals(5, result.getNumerator());
        assertEquals(4, result.getDenominator());
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        fraction.divide(new Fraction(0));
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(2, 4);
        assertTrue(fraction.equals(fraction1));
        assertTrue(fraction.equals(fraction2));
    }

    @Test
    public void testNotEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        assertFalse(fraction.equals(fraction1));
    }

    @Test
    public void testToString() {
        assertEquals("1 / 2", fraction.toString());
    }

    @Test
    public void testGetNumerator() {
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testGetDenominator() {
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetField() {
        assertEquals(FractionField.getInstance(), fraction.getField());
    }

}