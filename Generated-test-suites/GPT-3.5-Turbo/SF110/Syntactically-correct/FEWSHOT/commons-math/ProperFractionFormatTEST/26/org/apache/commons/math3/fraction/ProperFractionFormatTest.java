package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        NumberFormat numeratorFormat = format.getNumeratorFormat();
        NumberFormat denominatorFormat = format.getDenominatorFormat();
        
        assertEquals(FractionFormat.getDefaultNumberFormat(), wholeFormat);
        assertEquals(FractionFormat.getDefaultNumberFormat(), numeratorFormat);
        assertEquals(FractionFormat.getDefaultNumberFormat(), denominatorFormat);
    }

    @Test
    public void testConstructor() {
        NumberFormat format = new DecimalFormat("#");
        ProperFractionFormat fractionFormat = new ProperFractionFormat(format);

        assertEquals(format, fractionFormat.getWholeFormat());
        assertEquals(format, fractionFormat.getNumeratorFormat());
        assertEquals(format, fractionFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithFormats() {
        NumberFormat wholeFormat = new DecimalFormat("#");
        NumberFormat numeratorFormat = new DecimalFormat("#");
        NumberFormat denominatorFormat = new DecimalFormat("#");
        ProperFractionFormat fractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        assertEquals(wholeFormat, fractionFormat.getWholeFormat());
        assertEquals(numeratorFormat, fractionFormat.getNumeratorFormat());
        assertEquals(denominatorFormat, fractionFormat.getDenominatorFormat());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatNull() {
        ProperFractionFormat format = new ProperFractionFormat();
        format.setWholeFormat(null);
    }

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
        
        assertEquals(new Fraction(3, 4), fraction);
        assertEquals(source.length(), pos.getIndex());
    }
}