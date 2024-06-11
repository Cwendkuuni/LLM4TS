package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
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
        Fraction fraction = new Fraction(3, 4);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer expected = new StringBuffer();

        expected.append("3 / 4");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, toAppendTo, pos).toString());

        fraction = new Fraction(5, 2);

        expected.setLength(0);
        expected.append("2 1 / 2");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, toAppendTo, pos).toString());
        
        fraction = new Fraction(-7, 8);

        expected.setLength(0);
        expected.append("-7 / 8");
        assertEquals(expected.toString(), properFractionFormat.format(fraction, toAppendTo, pos).toString());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat numberFormat = properFractionFormat.getWholeFormat();
        assertNull(numberFormat);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        properFractionFormat.setWholeFormat(null);
    }

    @Test
    public void testParse() {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);
        Fraction expected = new Fraction(3, 4);
        assertEquals(expected, properFractionFormat.parse(source, pos));

        source = "2 1 / 2";
        expected = new Fraction(5, 2);
        pos = new ParsePosition(0);
        assertEquals(expected, properFractionFormat.parse(source, pos));
        
        source = "-7 / 8";
        expected = new Fraction(-7, 8);
        pos = new ParsePosition(0);
        assertEquals(expected, properFractionFormat.parse(source, pos));
    }
}