package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
   
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(3, 4);
        
        format.format(fraction, toAppendTo, pos);
        assertEquals("3 / 4", toAppendTo.toString());
        
        toAppendTo.setLength(0);
        fraction = new Fraction(-5, 4);
        
        format.format(fraction, toAppendTo, pos);
        assertEquals("-5 / 4", toAppendTo.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        NumberFormat wholeFormat = format.getWholeFormat();
        
        assertNull(wholeFormat);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = format.parse("3 / 4", pos);
        assertEquals(new Fraction(3, 4), fraction);
        
        pos.setIndex(0);
        fraction = format.parse("-5 / 4", pos);
        assertEquals(new Fraction(-5, 4), fraction);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        
        format.setWholeFormat(wholeFormat);
        
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}