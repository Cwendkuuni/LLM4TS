package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 5);
        assertEquals("Abs test failed", new Fraction(3, 5), fraction.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(3, 5);
        Fraction fraction2 = new Fraction(4, 7);
        assertTrue("CompareTo test failed", fraction1.compareTo(fraction2) > 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("DoubleValue test failed", 0.6666666666666666, fraction.doubleValue(), 1E-5);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(2, 4);
        Fraction fraction2 = new Fraction(1, 2);
        assertTrue("Equals test failed", fraction1.equals(fraction2));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("FloatValue test failed", 0.5, fraction.floatValue(), 1E-5);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 7);
        assertEquals("GetDenominator test failed", 7, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(2, 5);
        assertEquals("GetNumerator test failed", 2, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("HashCode test failed", 253, fraction.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals("IntValue test failed", 0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("LongValue test failed", 0, fraction.longValue());
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 5);
        assertEquals("Negate test failed", new Fraction(-3, 5), fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(5, 9);
        assertEquals("Reciprocal test failed", new Fraction(9, 5), fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals("Add test failed", new Fraction(5, 4), fraction1.add(fraction2));
    }

    @Test
    public void testAddInt() {
        Fraction fraction = new Fraction(2, 3);
        assertEquals("AddInt test failed", new Fraction(8, 3), fraction.add(2));
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(5, 6);
        Fraction fraction2 = new Fraction(2, 3);
        assertEquals("Subtract test failed", new Fraction(1, 6), fraction1.subtract(fraction2));
    }

    @Test
    public void testSubtractInt() {
        Fraction fraction = new Fraction(5, 6);
        assertEquals("SubtractInt test failed", new Fraction(-1, 6), fraction.subtract(1));
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals("Multiply test failed", new Fraction(1, 2), fraction1.multiply(fraction2));
    }

    @Test
    public void testMultiplyInt() {
        Fraction fraction = new Fraction(4, 5);
        assertEquals("MultiplyInt test failed", new Fraction(8, 5), fraction.multiply(2));
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals("Divide test failed", new Fraction(8, 9), fraction1.divide(fraction2));
    }

    @Test
    public void testDivideInt() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("DivideInt test failed", new Fraction(3, 8), fraction.divide(2));
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(1, 2);
        assertEquals("PercentageValue test failed", 50.0, fraction.percentageValue(), 1E-5);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction = Fraction.getReducedFraction(6, 9);
        assertEquals("GetReducedFraction test failed", new Fraction(2, 3), fraction);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);
        assertEquals("ToString test failed", "3 / 4", fraction.toString());
    }

}