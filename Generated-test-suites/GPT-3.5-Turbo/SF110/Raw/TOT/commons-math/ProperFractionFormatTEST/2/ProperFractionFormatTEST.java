
package org.apache.commons.math3.fraction;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

public class ProperFractionFormatTest {

    // Test case for ProperFractionFormat constructor
    @Test
    public void testProperFractionFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        assertNotNull(format);
        
        NumberFormat nf = NumberFormat.getNumberInstance();
        ProperFractionFormat formatWithNumberFormat = new ProperFractionFormat(nf);
        assertNotNull(formatWithNumberFormat);
        
        ProperFractionFormat formatWithWholeNumFormat = new ProperFractionFormat(nf, nf, nf);
        assertNotNull(formatWithWholeNumFormat);
    }
    
    // Test case for format() method with positive whole number
    @Test
    public void testFormat_PositiveWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(12, 1);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("12", sb.toString());
    }
    
    // Test case for format() method with positive proper fraction
    @Test
    public void testFormat_PositiveProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);       
        assertEquals("3 / 4", sb.toString());
    }
    
    // Test case for format() method with negative whole number
    @Test
    public void testFormat_NegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-5, 1);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-5", sb.toString());
    }
    
    // Test case for format() method with negative proper fraction
    @Test
    public void testFormat_NegativeProperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-2, 3);
        StringBuffer sb = new StringBuffer();
        format.format(fraction, sb, null);
        assertEquals("-2 / 3", sb.toString());
    }
    
    // Test case for getWholeFormat() method
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat nf = format.getWholeFormat();
        assertNotNull(nf);
        assertEquals(NumberFormat.getInstance(Locale.getDefault()), nf);
    }
    
    // Test case for parse() method with valid input
    @Test
    public void testParse_ValidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "5 / 6";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNotNull(fraction);
        assertEquals(5, fraction.getNumerator());
        assertEquals(6, fraction.getDenominator());
    }
    
    // Test case for parse() method with invalid input
    @Test
    public void testParse_InvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "A / B";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(input, pos);
        assertNull(fraction);
        assertEquals(0, pos.getErrorIndex());
    }
    
    // Test case for setWholeFormat() method
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        format.setWholeFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }
}

