package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FractionTest {
    @Test
    public void testConstructor1() {
        Fraction fraction = new Fraction(0);
        assertEquals(0, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test
    public void testConstructor2() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.add(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(1, result.getDenominator());
    }

    @Test
    public void testAddWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.add(1);
        assertEquals(5, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testSubtractWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.subtract(1);
        assertEquals(-1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(1, result.getNumerator());
        assertEquals(6, result.getDenominator());
    }

    @Test
    public void testMultiplyWithInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.multiply(2);
        assertEquals(4, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(8, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }

    @Test
    public void testDivideByInt() {
        Fraction fraction = new Fraction(2, 3);
        Fraction result = fraction.divide(2);
        assertEquals(1, result.getNumerator());
        assertEquals(3, result.getDenominator());
    }
}