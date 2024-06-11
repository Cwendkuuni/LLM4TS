package org.apache.commons.math3.fraction;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FractionTest {

    private Fraction fraction;

    @Before
    public void setUp() {
        fraction = new Fraction(1, 2);
    }

    @Test
    public void testAbs() {
        assertEquals(new Fraction(1, 2), fraction.abs());
        assertEquals(new Fraction(1, 2), fraction.negate().abs());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, fraction.compareTo(new Fraction(1, 2)));
        assertTrue(fraction.compareTo(new Fraction(1, 4)) > 0);
        assertTrue(fraction.compareTo(new Fraction(2, 3)) < 0);
    }

    @Test
    public void testDoubleValue() {
        assertEquals(0.5, fraction.doubleValue(), 0.0);
    }

    @Test
    public void testEquals() {
        assertFalse(fraction.equals(new Fraction(1, 4)));
        assertFalse(fraction.equals(new Object()));
        assertTrue(fraction.equals(new Fraction(1, 2)));
    }

    @Test
    public void testFloatValue() {
        assertEquals(0.5, fraction.floatValue(), 0.0);
    }

    @Test
    public void testGetDenominator() {
        assertEquals(2, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        assertEquals(1, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        assertEquals(961, fraction.hashCode());
    }

    @Test
    public void testIntValue() {
        assertEquals(0, fraction.intValue());
    }

    @Test
    public void testLongValue() {
        assertEquals(0L, fraction.longValue());
    }

    @Test
    public void testNegate() {
        assertEquals(new Fraction(-1, 2), fraction.negate());
    }

    @Test
    public void testReciprocal() {
        assertEquals(new Fraction(2, 1), fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        assertEquals(new Fraction(3, 2), fraction.add(new Fraction(1, 2)));
        assertEquals(new Fraction(5, 2), fraction.add(2));
    }

    @Test
    public void testSubtract() {
        assertEquals(new Fraction(-1, 2), fraction.subtract(new Fraction(1, 2)));
        assertEquals(new Fraction(-3, 2), fraction.subtract(2));
    }

    @Test
    public void testMultiply() {
        assertEquals(new Fraction(1, 4), fraction.multiply(new Fraction(1, 2)));
        assertEquals(new Fraction(1, 1), fraction.multiply(2));
    }

    @Test
    public void testDivide() {
        assertEquals(new Fraction(1, 1), fraction.divide(new Fraction(1, 2)));
        assertEquals(new Fraction(1, 4), fraction.divide(2));
    }

    @Test
    public void testPercentageValue() {
        assertEquals(50.0, fraction.percentageValue(), 0.0);
    }

    @Test
    public void testGetReducedFraction() {
        assertEquals(new Fraction(1, 2), Fraction.getReducedFraction(2, 4));
        assertEquals(new Fraction(-3, 2), Fraction.getReducedFraction(9, -6));
    }

    @Test
    public void testToString() {
        assertEquals("1 / 2", fraction.toString());
    }

    @Test
    public void testGetField() {
        assertEquals(FractionField.getInstance(), fraction.getField());
    }

}