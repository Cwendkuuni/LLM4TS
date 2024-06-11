package org.apache.commons.math.fraction;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.NumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("3 / 4", buffer.toString());
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
    
    // Potential Edge Cases:
    // - Whole number is negative
    // - Numerator is zero
    // - Denominator is zero
    // - Denominator is negative
    
    @Test
    public void testFormat_NegativeWholeNumber() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(-3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("-3 / 4", buffer.toString());
    }
    
    @Test
    public void testFormat_NumeratorZero() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(0, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
        assertEquals("0 / 4", buffer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormat_DenominatorZero() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 0);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        format.format(fraction, buffer, pos);
    }
    
    @Test
    public void testParse_DenominatorNegative() {
        ProperFractionFormat format = new ProperFractionFormat();
        String source = "3 / -4";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(-3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }
}