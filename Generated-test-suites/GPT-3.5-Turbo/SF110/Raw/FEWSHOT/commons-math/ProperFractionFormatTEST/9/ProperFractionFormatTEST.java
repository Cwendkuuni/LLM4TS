package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        
        Fraction fraction1 = new Fraction(3, 4);
        format.format(fraction1, buffer, pos);
        assertEquals("3 / 4", buffer.toString());
        
        Fraction fraction2 = new Fraction(5, 2);
        format.format(fraction2, buffer, pos);
        assertEquals("2 1 / 2", buffer.toString());
        
        Fraction fraction3 = new Fraction(0, 9);
        format.format(fraction3, buffer, pos);
        assertEquals("0", buffer.toString());
        
        buffer.setLength(0);
        Fraction fraction4 = new Fraction(-7, 8);
        format.format(fraction4, buffer, pos);
        assertEquals("-7 / 8", buffer.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertNull(wholeFormat);
        
        NumberFormat customFormat = NumberFormat.getInstance();
        format.setWholeFormat(customFormat);
        assertEquals(customFormat, format.getWholeFormat());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        String source1 = "3 / 4";
        ParsePosition pos1 = new ParsePosition(0);
        Fraction fraction1 = format.parse(source1, pos1);
        assertEquals(new Fraction(3, 4), fraction1);
        assertEquals(6, pos1.getIndex());
        
        String source2 = "2 5 / 6";
        ParsePosition pos2 = new ParsePosition(0);
        Fraction fraction2 = format.parse(source2, pos2);
        assertEquals(new Fraction(17, 6), fraction2);
        assertEquals(9, pos2.getIndex());
        
        String source3 = "0";
        ParsePosition pos3 = new ParsePosition(0);
        Fraction fraction3 = format.parse(source3, pos3);
        assertEquals(new Fraction(0), fraction3);
        assertEquals(1, pos3.getIndex());
        
        String source4 = "9 / 1";
        ParsePosition pos4 = new ParsePosition(0);
        Fraction fraction4 = format.parse(source4, pos4);
        assertEquals(new Fraction(9), fraction4);
        assertEquals(4, pos4.getIndex());
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat customFormat = NumberFormat.getInstance();
        format.setWholeFormat(customFormat);
        assertEquals(customFormat, format.getWholeFormat());
    }
}