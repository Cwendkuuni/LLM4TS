
package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        FieldPosition fpos = new FieldPosition(0);
        StringBuffer buffer = new StringBuffer();
        Fraction fraction = new Fraction(2, 3);
        
        properFractionFormat.format(fraction, buffer, fpos);
        
        assertEquals("2 / 3", buffer.toString());
        assertEquals(0, fpos.getBeginIndex());
        assertEquals(0, fpos.getEndIndex());
    }
    
    @Test
    public void testFormatWithWholeNumber() {
        FieldPosition fpos = new FieldPosition(0);
        StringBuffer buffer = new StringBuffer();
        Fraction fraction = new Fraction(5, 3);
        
        properFractionFormat.format(fraction, buffer, fpos);
        
        assertEquals("1 2 / 3", buffer.toString());
        assertEquals(0, fpos.getBeginIndex());
        assertEquals(0, fpos.getEndIndex());
    }
    
    @Test
    public void testFormatWithNegativeWholeNumber() {
        FieldPosition fpos = new FieldPosition(0);
        StringBuffer buffer = new StringBuffer();
        Fraction fraction = new Fraction(-5, 3);
        
        properFractionFormat.format(fraction, buffer, fpos);
        
        assertEquals("-1 2 / 3", buffer.toString());
        assertEquals(0, fpos.getBeginIndex());
        assertEquals(0, fpos.getEndIndex());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = new NumberFormat();
        properFractionFormat.setWholeFormat(wholeFormat);
        
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
    
    @Test
    public void testParse() {
        String source = "2 / 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = properFractionFormat.parse(source, pos);
        
        assertEquals(2, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithWholeNumber() {
        String source = "1 2 / 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = properFractionFormat.parse(source, pos);
        
        assertEquals(5, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testParseWithNegativeWholeNumber() {
        String source = "-1 2 / 3";
        ParsePosition pos = new ParsePosition(0);
        
        Fraction fraction = properFractionFormat.parse(source, pos);
        
        assertEquals(-5, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());
        assertEquals(source.length(), pos.getIndex());
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = new NumberFormat();
        properFractionFormat.setWholeFormat(wholeFormat);
        
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }
}
