package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        assertEquals("3/4", format.format(fraction));
    }

    @Test
    public void testFormatWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = new Fraction(7, 4);
        assertEquals("1 3/4", format.format(fraction));
    }

    @Test
    public void testFormatWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        assertEquals("-3/4", format.format(fraction));
    }

    @Test
    public void testFormatWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = new Fraction(-7, 4);
        assertEquals("-1 3/4", format.format(fraction));
    }

    @Test
    public void testFormatWithWholeNumberAndNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = new Fraction(-11, 4);
        assertEquals("-2 3/4", format.format(fraction));
    }

    @Test
    public void testFormatWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 4);
        assertEquals("0", format.format(fraction));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("3/4");
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, fraction);
    }

    @Test
    public void testParseWithWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = format.parse("1 3/4");
        Fraction expected = new Fraction(7, 4);
        assertEquals(expected, fraction);
    }

    @Test
    public void testParseWithNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("-3/4");
        Fraction expected = new Fraction(-3, 4);
        assertEquals(expected, fraction);
    }

    @Test
    public void testParseWithNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = format.parse("-1 3/4");
        Fraction expected = new Fraction(-7, 4);
        assertEquals(expected, fraction);
    }

    @Test
    public void testParseWithWholeNumberAndNegativeFraction() {
        ProperFractionFormat format = new ProperFractionFormat(new DecimalFormat("0"), new DecimalFormat("0"), new DecimalFormat("0"));
        Fraction fraction = format.parse("-2 3/4");
        Fraction expected = new Fraction(-11, 4);
        assertEquals(expected, fraction);
    }

    @Test
    public void testParseWithZeroFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = format.parse("0");
        Fraction expected = new Fraction(0, 1);
        assertEquals(expected, fraction);
    }
}