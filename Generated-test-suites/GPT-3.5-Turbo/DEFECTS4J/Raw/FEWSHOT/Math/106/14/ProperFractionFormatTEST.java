package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, toAppendTo, pos);
        assertEquals("3/4", toAppendTo.toString());
        
        fraction = new Fraction(5, 2);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, toAppendTo, pos);
        assertEquals("2 1/2", toAppendTo.toString());
        
        fraction = new Fraction(-1, 3);
        toAppendTo = new StringBuffer();
        pos = new FieldPosition(0);
        format.format(fraction, toAppendTo, pos);
        assertEquals("-1/3", toAppendTo.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = format.parse("3/4", pos);
        assertEquals(new Fraction(3, 4), fraction);
        
        fraction = format.parse("2 1/2", pos);
        assertEquals(new Fraction(5, 2), fraction);
        
        fraction = format.parse("-1/3", pos);
        assertEquals(new Fraction(-1, 3), fraction);
        
        pos = new ParsePosition(0);
        fraction = format.parse("5", pos);
        assertNull(fraction);
        
        pos = new ParsePosition(0);
        fraction = format.parse("2 1/2/3", pos);
        assertNull(fraction);
        
        pos = new ParsePosition(0);
        fraction = format.parse("2 1/a", pos);
        assertNull(fraction);
    }
}