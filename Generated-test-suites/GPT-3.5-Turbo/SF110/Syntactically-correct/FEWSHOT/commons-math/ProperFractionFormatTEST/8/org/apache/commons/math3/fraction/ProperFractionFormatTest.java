package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    @Test
    public void testConstructor1() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertEquals(NumberFormat.getInstance(), format.getNumeratorFormat());
        assertEquals(NumberFormat.getInstance(), format.getDenominatorFormat());
        assertEquals(NumberFormat.getInstance(), format.getWholeFormat());
    }
    
    @Test
    public void testConstructor2() {
        NumberFormat format1 = NumberFormat.getIntegerInstance();
        NumberFormat format2 = NumberFormat.getIntegerInstance();
        ProperFractionFormat format = new ProperFractionFormat(format1, format2, format2);
        assertEquals(format1, format.getWholeFormat());
        assertEquals(format2, format.getNumeratorFormat());
        assertEquals(format2, format.getDenominatorFormat());
    }
    
    @Test
    public void testConstructor3() {
        NumberFormat format1 = NumberFormat.getIntegerInstance();
        NumberFormat format2 = NumberFormat.getIntegerInstance();
        ProperFractionFormat format = new ProperFractionFormat(format1, format2, format2);
        format.setWholeFormat(NumberFormat.getCurrencyInstance());
        assertEquals(NumberFormat.getCurrencyInstance(), format.getWholeFormat());
    }
    
    @Test
    public void testFormat1() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(1, 2);
        format.format(fraction, buffer, pos);
        assertEquals("1 / 2", buffer.toString());
    }
    
    @Test
    public void testFormat2() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getCurrencyInstance());
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(5, 2);
        format.format(fraction, buffer, pos);
        assertEquals("$5 1 / 2", buffer.toString());
    }
    
    @Test
    public void testParse1() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 / 2", pos);
        assertEquals(new Fraction(1, 2), fraction);
    }
    
    @Test
    public void testParse2() {
        ProperFractionFormat format = new ProperFractionFormat(NumberFormat.getCurrencyInstance());
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("$5 1 / 2", pos);
        assertEquals(new Fraction(5, 2), fraction);
    }
    
    @Test
    public void testParse3() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 ", pos);
        assertNull(fraction);
    }
    
    @Test
    public void testParse4() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("0 /", pos);
        assertNull(fraction);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat format1 = NumberFormat.getIntegerInstance();
        format.setWholeFormat(format1);
        assertEquals(format1, format.getWholeFormat());
    }
    
    @Test(expected=NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }
}