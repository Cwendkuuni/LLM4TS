package org.apache.commons.math3.fraction;

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
        StringBuffer buffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        
        Fraction fraction1 = new Fraction(1, 2);
        format.format(fraction1, buffer, fieldPosition);
        assertEquals("1 / 2", buffer.toString());
        
        Fraction fraction2 = new Fraction(2, 4);
        buffer.setLength(0);
        format.format(fraction2, buffer, fieldPosition);
        assertEquals("1 / 2", buffer.toString());
        
        Fraction fraction3 = new Fraction(3, 1);
        buffer.setLength(0);
        format.format(fraction3, buffer, fieldPosition);
        assertEquals("3", buffer.toString());
        
        Fraction fraction4 = new Fraction(0, 3);
        buffer.setLength(0);
        format.format(fraction4, buffer, fieldPosition);
        assertEquals("0", buffer.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source1 = "1 / 2";
        String source2 = " 3";
        String source3 = "4 / 5";
        String source4 = "6";
        
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction1 = format.parse(source1, pos);
        assertEquals(1, fraction1.getNumerator());
        assertEquals(2, fraction1.getDenominator());
        
        pos.setIndex(0);
        Fraction fraction2 = format.parse(source2, pos);
        assertEquals(3, fraction2.getNumerator());
        assertEquals(1, fraction2.getDenominator());
        
        pos.setIndex(0);
        Fraction fraction3 = format.parse(source3, pos);
        assertEquals(4, fraction3.getNumerator());
        assertEquals(5, fraction3.getDenominator());
        
        pos.setIndex(0);
        Fraction fraction4 = format.parse(source4, pos);
        assertEquals(6, fraction4.getNumerator());
        assertEquals(1, fraction4.getDenominator());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        assertEquals(NumberFormat.getInstance(), wholeFormat);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getCurrencyInstance();
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
}