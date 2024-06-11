package org.apache.commons.math.fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    @Test
    public void testFormatFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        Fraction fraction = new Fraction(1, 2);
        format.format(fraction, buffer, pos);
        assertEquals("1 / 2", buffer.toString());
    }
    
    @Test
    public void testParseFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("1 / 2", pos);
        assertEquals(1, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testParseImproperFraction() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = new Fraction(1, 2);
        Fraction result = format.parse("0.5", pos);
        assertEquals(fraction, result);
    }
    
    @Test
    public void testParseNegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("-1 1/2", pos);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
    }
    
    @Test
    public void testParseInvalidInput() {
        ProperFractionFormat format = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse("3 1 2", pos);
        assertNull(fraction);
    }
}