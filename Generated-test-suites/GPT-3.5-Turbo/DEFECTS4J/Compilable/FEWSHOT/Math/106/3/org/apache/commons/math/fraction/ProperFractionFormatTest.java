package org.apache.commons.math.fraction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test valid proper fraction
        Fraction fraction = new Fraction(1, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("1 / 2", buffer.toString());
        
        // Test valid proper fraction with whole number
        fraction = new Fraction(3, 2);
        buffer = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("1 1 / 2", buffer.toString());
        
        // Test valid proper fraction with negative whole number
        fraction = new Fraction(-3, 2);
        buffer = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("-1 1 / 2", buffer.toString());
        
        // Test fraction with whole number as 0
        fraction = new Fraction(0, 3);
        buffer = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("0 / 3", buffer.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        // Test valid proper fraction
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(new Fraction(1, 2), fraction);
        assertEquals(source.length(), pos.getIndex());
        
        // Test valid proper fraction with whole number
        source = "1 1 / 2";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        assertEquals(new Fraction(3, 2), fraction);
        assertEquals(source.length(), pos.getIndex());
        
        // Test valid proper fraction with negative whole number
        source = "-1 1 / 2";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        assertEquals(new Fraction(-3, 2), fraction);
        assertEquals(source.length(), pos.getIndex());
        
        // Test fraction with whole number as 0
        source = "0 / 3";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        assertEquals(new Fraction(0, 3), fraction);
        assertEquals(source.length(), pos.getIndex());
        
        // Test invalid proper fraction without '/'
        source = "1 2";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        
        // Test invalid proper fraction with invalid '/'
        source = "1 / 2 /";
        pos = new ParsePosition(0);
        fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
    }
}