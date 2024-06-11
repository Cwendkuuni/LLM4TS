package org.apache.commons.math3.fraction;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat fractionFormat;
    
    @Before
    public void setup() {
        fractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        String expected = "3 / 4";
        String actual = fractionFormat.format(fraction);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-5, 6);
        String expected = "-5 / 6";
        String actual = fractionFormat.format(fraction);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(7, 1);
        String expected = "7";
        String actual = fractionFormat.format(fraction);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithZero() {
        Fraction fraction = new Fraction(0, 1);
        String expected = "0";
        String actual = fractionFormat.format(fraction);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatWithMixedFraction() {
        Fraction fraction = new Fraction(9, 5);
        String expected = "1 4 / 5";
        String actual = fractionFormat.format(fraction);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParse() {
        String source = "5 / 6";
        Fraction expected = new Fraction(5, 6);
        Fraction actual = fractionFormat.parse(source);
        Assert.assertTrue(Precision.equals(expected.doubleValue(), actual.doubleValue(), 0.0001));
    }
    
    @Test
    public void testParseWithNegativeFraction() {
        String source = "-3 / 4";
        Fraction expected = new Fraction(-3, 4);
        Fraction actual = fractionFormat.parse(source);
        Assert.assertTrue(Precision.equals(expected.doubleValue(), actual.doubleValue(), 0.0001));
    }
    
    @Test
    public void testParseWithWholeNumber() {
        String source = "10";
        Fraction expected = new Fraction(10, 1);
        Fraction actual = fractionFormat.parse(source);
        Assert.assertTrue(Precision.equals(expected.doubleValue(), actual.doubleValue(), 0.0001));
    }
    
    @Test
    public void testParseWithMixedFraction() {
        String source = "3 2 / 5";
        Fraction expected = new Fraction(17, 5);
        Fraction actual = fractionFormat.parse(source);
        Assert.assertTrue(Precision.equals(expected.doubleValue(), actual.doubleValue(), 0.0001));
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
        fractionFormat.setWholeFormat(numberFormat);
        Assert.assertSame(numberFormat, fractionFormat.getWholeFormat());
    }
}