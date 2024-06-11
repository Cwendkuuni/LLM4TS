package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(1, 2);
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        format.format(fraction, result, pos);
        
        assertEquals("1 / 2", result.toString());
        assertEquals(0, pos.getBeginIndex());
        assertEquals(0, pos.getEndIndex());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = format.parse(source, pos);
        
        assertEquals(new Fraction(1, 2), result);
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParse_invalidWholenumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "-1 / 2";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = format.parse(source, pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
    
    @Test
    public void testParse_invalidDenominator() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "1 / -2";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = format.parse(source, pos);
        
        assertNull(result);
        assertEquals(0, pos.getIndex());
        assertEquals(0, pos.getErrorIndex());
    }
}