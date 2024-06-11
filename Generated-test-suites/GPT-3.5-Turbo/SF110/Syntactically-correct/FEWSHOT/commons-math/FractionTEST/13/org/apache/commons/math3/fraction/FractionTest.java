package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FractionTest {

    @Test
    public void testFractionConstructor() {
        Fraction fraction = new Fraction(5, 2);
        assertEquals(5, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());

        fraction = new Fraction(1);
        assertEquals(1, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testZeroDenominator() {
        Fraction fraction = new Fraction(5, 0);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(3, 4);
        Fraction fraction3 = new Fraction(1, 2);

        assertEquals(fraction1, fraction2);
        assertNotEquals(fraction1, fraction3);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(5, 12);

        assertEquals(fraction3, fraction1.add(fraction2));
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(1, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(-5, 12);

        assertEquals(fraction3, fraction1.subtract(fraction2));
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(1, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(1, 6);

        assertEquals(fraction3, fraction1.multiply(fraction2));
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(1, 4);
        Fraction fraction2 = new Fraction(2, 3);
        Fraction fraction3 = new Fraction(3, 8);

        assertEquals(fraction3, fraction1.divide(fraction2));
    }

    @Test
    public void testAbs() {
        Fraction fraction1 = new Fraction(4, 5);
        Fraction fraction2 = new Fraction(-4, 5);

        assertEquals(fraction1, fraction1.abs());
        assertEquals(fraction1, fraction2.abs());
    }

    @Test(expected = MathArithmeticException.class)
    public void testNegate() {
        Fraction fraction = new Fraction(Integer.MIN_VALUE, 1);
        fraction.negate();
    }

    @Test
    public void testReciprocal() {
        Fraction fraction1 = new Fraction(4, 5);
        Fraction fraction2 = new Fraction(5, 4);

        assertEquals(fraction2, fraction1.reciprocal());
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("2 / 3", fraction.toString());
    }

}