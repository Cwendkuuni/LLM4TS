package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.NumberFormat;

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
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("3 / 4", buffer.toString());
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        Fraction fraction = new Fraction(5, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("1 1/4", buffer.toString());
    }
    
    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);
        format.format(fraction, buffer, position);
        assertEquals("-3 / 4", buffer.toString());
    }
    
    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertEquals(new Fraction(3, 4), fraction);
    }
    
    @Test
    public void testParseWithWholeNumber() {
        String source = "1 1/4";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertEquals(new Fraction(5, 4), fraction);
    }
    
    @Test
    public void testParseWithNegativeFraction() {
        String source = "-3 / 4";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertEquals(new Fraction(-3, 4), fraction);
    }
    
    @Test
    public void testParseInvalidFormat() {
        String source = "3 / 4 /";
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(source, position);
        assertEquals(null, fraction);
    }
}