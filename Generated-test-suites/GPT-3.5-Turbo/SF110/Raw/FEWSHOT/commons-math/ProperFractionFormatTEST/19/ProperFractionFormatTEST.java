package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction1 = new Fraction(3, 4);
        format.format(fraction1, sb, pos);
        assertEquals("3 / 4", sb.toString());
        
        sb = new StringBuffer();
        Fraction fraction2 = new Fraction(5, 2);
        format.format(fraction2, sb, pos);
        assertEquals("2 1 / 2", sb.toString());
        
        sb = new StringBuffer();
        Fraction fraction3 = new Fraction(-2, 3);
        format.format(fraction3, sb, pos);
        assertEquals("-2 / 3", sb.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNull(wholeFormat);
        
        NumberFormat customWholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(customWholeFormat);
        assertEquals(customWholeFormat, format.getWholeFormat());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction1 = format.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), fraction1);
        
        pos = new ParsePosition(0);
        Fraction fraction2 = format.parse("2 1 / 2", pos);
        assertEquals(new Fraction(5, 2), fraction2);
        
        pos = new ParsePosition(0);
        Fraction fraction3 = format.parse("-2 / 3", pos);
        assertEquals(new Fraction(-2, 3), fraction3);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat customWholeFormat = NumberFormat.getInstance();
        format.setWholeFormat(customWholeFormat);
        assertEquals(customWholeFormat, format.getWholeFormat());
    }
}