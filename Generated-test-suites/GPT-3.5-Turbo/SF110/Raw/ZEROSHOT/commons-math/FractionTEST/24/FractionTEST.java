package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(5, result.getNumerator());
        assertEquals(6, result.getDenominator());

        fraction1 = new Fraction(-1, 2);
        fraction2 = new Fraction(1, 3);
        result = fraction1.add(fraction2);
        assertEquals(-1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());

        fraction1 = new Fraction(-1, 2);
        fraction2 = new Fraction(1, 3);
        result = fraction1.subtract(fraction2);
        assertEquals(-5, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());

        fraction1 = new Fraction(-1, 2);
        fraction2 = new Fraction(1, 3);
        result = fraction1.multiply(fraction2);
        assertEquals(-1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());

        fraction1 = new Fraction(-1, 2);
        fraction2 = new Fraction(1, 3);
        result = fraction1.divide(fraction2);
        assertEquals(-3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }
}