package org.apache.commons.math.fraction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 / 2", result.toString());
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        assertEquals("-3 / 4", result.toString());
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        assertEquals("1 1 / 2", result.toString());
    }
    
    @Test
    public void testFormatWithNegativeWholeNumber() {
        Fraction fraction = new Fraction(-3, 2);
        StringBuffer result = format.format(fraction, new StringBuffer(), null);
        assertEquals("-1 1 / 2", result.toString());
    }

    @Test
    public void testParse() {
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(1, 2), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithNegativeFraction() {
        String source = "-3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(-3, 4), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithWholeNumber() {
        String source = "1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(3, 2), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithNegativeWholeNumber() {
        String source = "-1 1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = format.parse(source, pos);
        assertEquals(new Fraction(-3, 2), result);
        assertEquals(source.length(), pos.getIndex());
    }
}