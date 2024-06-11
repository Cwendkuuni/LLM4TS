package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    private NumberFormat numberFormat;

    @Before
    public void setup() {
        numberFormat = NumberFormat.getInstance();
        properFractionFormat = new ProperFractionFormat(numberFormat);
    }

    @Test
    public void testFormat() {
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(2, 3);
        StringBuffer expected = new StringBuffer();
        expected.append("2 / 3");
        
        properFractionFormat.format(fraction, toAppendTo, pos);
        
        assertEquals(expected.toString(), toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        
        assertNotNull(wholeFormat);
        assertEquals(numberFormat, wholeFormat);
    }

    @Test
    public void testParse_ValidFraction() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(2, 3);
        
        Fraction result = properFractionFormat.parse(source, pos);
        
        assertEquals(expected, result);
    }

    @Test
    public void testParse_InvalidFraction() {
        String source = "2/3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction result = properFractionFormat.parse(source, pos);
        
        assertNull(result);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat newFormat = NumberFormat.getInstance();
        
        properFractionFormat.setWholeFormat(newFormat);
        
        assertEquals(newFormat, properFractionFormat.getWholeFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormat_NullArgument() {
        properFractionFormat.setWholeFormat(null);
    }

}