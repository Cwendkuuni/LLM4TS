package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();

        Fraction fraction1 = new Fraction(1, 2);
        assertEquals("1/2", format.format(fraction1));
        
        Fraction fraction2 = new Fraction(3, 2);
        assertEquals("1 1/2", format.format(fraction2));
        
        Fraction fraction3 = new Fraction(-5, 2);
        assertEquals("-2 1/2", format.format(fraction3));
        
        Fraction fraction4 = new Fraction(-3, 4);
        assertEquals("-3/4", format.format(fraction4));
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        
        // valid format
        Fraction fraction1 = format.parse("1/2", pos);
        assertEquals(new Fraction(1, 2), fraction1);
        
        pos.setIndex(0);
        Fraction fraction2 = format.parse("1 1/2", pos);
        assertEquals(new Fraction(3, 2), fraction2);
        
        pos.setIndex(0);
        Fraction fraction3 = format.parse("-2 1/2", pos);
        assertEquals(new Fraction(-5, 2), fraction3);
        
        pos.setIndex(0);
        Fraction fraction4 = format.parse("-3/4", pos);
        assertEquals(new Fraction(-3, 4), fraction4);
        
        // invalid format
        pos.setIndex(0);
        Fraction fraction5 = format.parse("1/2/3", pos);
        assertNull(fraction5);
        
        pos.setIndex(0);
        Fraction fraction6 = format.parse("", pos);
        assertNull(fraction6);
    }

    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        
        // validate default whole format
        assertEquals(NumberFormat.getInstance(), wholeFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat newWholeFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(newWholeFormat);
        
        // validate new whole format
        assertEquals(newWholeFormat, format.getWholeFormat());
    }
}