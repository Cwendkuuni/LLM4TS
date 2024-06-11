package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.text.NumberFormat;
import java.text.ParseException;
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
        Fraction fraction = new Fraction(3, 4);
        StringBuffer sb = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        String expected = "3 / 4";
        assertEquals(expected, properFractionFormat.format(fraction, sb, pos).toString());
    }

    @Test
    public void testParse() {
        String source = "2 / 5";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNotNull(fraction);
        assertEquals(2, fraction.getNumerator());
        assertEquals(5, fraction.getDenominator());
    }

    @Test
    public void testWholeFormatSetterGetter() {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testInvalidFractionParse() {
        String source = "2 / -3";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = properFractionFormat.parse(source, pos);
        assertNull(fraction);
    }

    @Test
    public void testNullWholeFormatSetter() {
        try {
            properFractionFormat.setWholeFormat(null);
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

}