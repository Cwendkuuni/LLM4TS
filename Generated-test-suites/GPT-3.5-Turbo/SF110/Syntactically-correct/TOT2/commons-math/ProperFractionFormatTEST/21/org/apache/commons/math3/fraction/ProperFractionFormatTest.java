package org.apache.commons.math3.fraction;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.fraction.ProperFractionFormat;
import org.apache.commons.math3.fraction.Fraction;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    @Test
    public void testProperFractionFormatConstructor1() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testProperFractionFormatConstructor2() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testProperFractionFormatConstructor3() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
        assertNotNull(properFractionFormat);
    }
    
    @Test
    public void testFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(2, 3);
        StringBuffer buffer = new StringBuffer();
        assertEquals("2 / 3", properFractionFormat.format(fraction, buffer, null).toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = properFractionFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse("2 / 3", pos);
        assertEquals(new Fraction(2, 3), fraction);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat properFractionFormat = new ProperFractionFormat();
        NumberFormat format = NumberFormat.getInstance();
        properFractionFormat.setWholeFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }
}