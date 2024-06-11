package org.apache.commons.math3.fraction;

import org.junit.Test;
import org.junit.Assert;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction1 = new Fraction(5, 2);
        Fraction fraction2 = new Fraction(-5, 2);

        Assert.assertEquals(new Fraction(5, 2), fraction1.abs());
        Assert.assertEquals(new Fraction(5, 2), fraction2.abs());
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(5, 2);
        Fraction fraction2 = new Fraction(4, 2);
        Fraction fraction3 = new Fraction(6, 2);

        Assert.assertEquals(1, fraction1.compareTo(fraction2));
        Assert.assertEquals(-1, fraction2.compareTo(fraction3));
        Assert.assertEquals(0, fraction1.compareTo(fraction1));
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(0.75, fraction.doubleValue(), 0);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);

        Assert.assertTrue(fraction1.equals(fraction2));
        Assert.assertFalse(fraction1.equals(new Object()));
    }

    @Test
    public void testFloatValue() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(0.75, fraction.floatValue(), 0);
    }

    @Test
    public void testGetDenominator() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(4, fraction.getDenominator());
    }

    @Test
    public void testGetNumerator() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(3, fraction.getNumerator());
    }

    @Test
    public void testHashCode() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);

        Assert.assertEquals(fraction1.hashCode(), fraction2.hashCode());
    }

    @Test
    public void testIntValue() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(0, fraction.intValue(), 0);
    }

    @Test
    public void testLongValue() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(0, fraction.longValue(), 0);
    }

    @Test
    public void testNegate() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(new Fraction(-3, 4), fraction.negate());
        Assert.assertNotEquals(fraction, fraction.negate());
    }

    @Test
    public void testReciprocal() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(new Fraction(4, 3), fraction.reciprocal());
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 5);

        Assert.assertEquals(new Fraction(23, 20), fraction1.add(fraction2));
        Assert.assertEquals(new Fraction(17, 10), fraction1.add(1));
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 5);

        Assert.assertEquals(new Fraction(7, 20), fraction1.subtract(fraction2));
        Assert.assertEquals(new Fraction(1, 4), fraction1.subtract(1));
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 5);

        Assert.assertEquals(new Fraction(3, 10), fraction1.multiply(fraction2));
        Assert.assertEquals(new Fraction(9, 4), fraction1.multiply(3));
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(2, 5);

        Assert.assertEquals(new Fraction(15, 8), fraction1.divide(fraction2));
        Assert.assertEquals(new Fraction(1, 3), fraction1.divide(4));
    }

    @Test
    public void testPercentageValue() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals(75, fraction.percentageValue(), 0);
    }

    @Test
    public void testGetReducedFraction() {
        Fraction fraction1 = Fraction.getReducedFraction(3, 4);
        Fraction fraction2 = Fraction.getReducedFraction(6, 8);

        Assert.assertEquals(new Fraction(3, 4), fraction1);
        Assert.assertEquals(fraction1, fraction2);
    }

    @Test
    public void testToString() {
        Fraction fraction = new Fraction(3, 4);

        Assert.assertEquals("3 / 4", fraction.toString());
    }
}