package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(2);
        assertEquals(2, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(2, 0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithOverflow() {
        new Fraction(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-2);
        Fraction absFrac = fraction.abs();
        assertEquals(2, absFrac.getNumerator());
        assertEquals(1, absFrac.getDenominator());

        fraction = new Fraction(1, -4);
        absFrac = fraction.abs();
        assertEquals(1, absFrac.getNumerator());
        assertEquals(4, absFrac.getDenominator());

        fraction = new Fraction(0);
        absFrac = fraction.abs();
        assertEquals(0, absFrac.getNumerator());
        assertEquals(1, absFrac.getDenominator());
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(4, 6);
        assertEquals(fraction1, fraction2);

        fraction1 = new Fraction(2, 3);
        fraction2 = new Fraction(1, 2);
        assertNotEquals(fraction1, fraction2);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction product = fraction1.multiply(fraction2);
        assertEquals(1, product.getNumerator());
        assertEquals(2, product.getDenominator());

        fraction1 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(-3, 4);
        product = fraction1.multiply(fraction3);
        assertEquals(-1, product.getNumerator());
        assertEquals(2, product.getDenominator());

        fraction2 = new Fraction(1, 4);
        product = fraction2.multiply(3);
        assertEquals(3, product.getNumerator());
        assertEquals(4, product.getDenominator());
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction quotient = fraction1.divide(fraction2);
        assertEquals(8, quotient.getNumerator());
        assertEquals(9, quotient.getDenominator());

        fraction1 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(-3, 4);
        quotient = fraction1.divide(fraction3);
        assertEquals(-8, quotient.getNumerator());
        assertEquals(9, quotient.getDenominator());

        fraction2 = new Fraction(1, 4);
        quotient = fraction2.divide(2);
        assertEquals(1, quotient.getNumerator());
        assertEquals(8, quotient.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(0);
        fraction1.divide(fraction2);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("2 / 3", fraction.toString());

        fraction = new Fraction(1, 2);
        assertEquals("1 / 2", fraction.toString());
    }
}