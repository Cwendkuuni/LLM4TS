
package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;
    
    @Before
    public void setUp() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        properFractionFormat = new ProperFractionFormat(df);
    }
    
    @Test
    public void testFormat() {
        Fraction fraction1 = new Fraction(3, 4);
        assertEquals("0.75", properFractionFormat.format(fraction1));
        
        Fraction fraction2 = new Fraction(4, 3);
        assertEquals("1 1/3", properFractionFormat.format(fraction2));
        
        Fraction fraction3 = new Fraction(5, 2);
        assertEquals("2 1/2", properFractionFormat.format(fraction3));
    }
    
    @Test
    public void testParse() {
        String fraction1 = "0.75";
        ParsePosition pos1 = new ParsePosition(0);
        assertEquals(new Fraction(3, 4), properFractionFormat.parse(fraction1, pos1));
        
        String fraction2 = "1 1/3";
        ParsePosition pos2 = new ParsePosition(0);
        assertEquals(new Fraction(4, 3), properFractionFormat.parse(fraction2, pos2));
        
        String fraction3 = "2 1/2";
        ParsePosition pos3 = new ParsePosition(0);
        assertEquals(new Fraction(5, 2), properFractionFormat.parse(fraction3, pos3));
    }
    
    @Test
    public void testParseInvalidInput() {
        String fraction1 = "0.75 1/2";
        ParsePosition pos1 = new ParsePosition(0);
        assertNull(properFractionFormat.parse(fraction1, pos1));
        
        String fraction2 = "1-1/3";
        ParsePosition pos2 = new ParsePosition(0);
        assertNull(properFractionFormat.parse(fraction2, pos2));
        
        String fraction3 = "2 1/2/3";
        ParsePosition pos3 = new ParsePosition(0);
        assertNull(properFractionFormat.parse(fraction3, pos3));
    }
}
