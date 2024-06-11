
package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat format;

    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("1 1/2", buffer.toString());
        
        fraction = new Fraction(12, 4);
        buffer.setLength(0);
        format.format(fraction, buffer, null);
        assertEquals("3", buffer.toString());
        
        fraction = new Fraction(5, 4);
        buffer.setLength(0);
        format.format(fraction, buffer, null);
        assertEquals("1 1/4", buffer.toString());
    }

    @Test
    public void testParse() {
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 1/2", pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        
        pos = new ParsePosition(0);
        fraction = format.parse("3", pos);
        assertEquals(12, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        
        pos = new ParsePosition(0);
        fraction = format.parse("1 1/4", pos);
        assertEquals(5, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
        
        pos = new ParsePosition(0);
        fraction = format.parse("1-1/2", pos);
        assertNull(fraction);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormat_NullFormat() {
        format.setWholeFormat(null);
    }
    
    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        format.setWholeFormat(numberFormat);
        assertEquals(numberFormat, format.getWholeFormat());
    }
}
