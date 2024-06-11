package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

public class ProperFractionFormatTest {

    private ProperFractionFormat formatter;

    @Before
    public void setUp() {
        this.formatter = new ProperFractionFormat();
    }

    @Test
    public void testDefaultConstructor() {
        ProperFractionFormat defaultFormatter = new ProperFractionFormat();

        NumberFormat numeratorFormat = defaultFormatter.getNumeratorFormat();
        assertNotNull(numeratorFormat);
        assertTrue(numeratorFormat instanceof NumberFormat);

        NumberFormat denominatorFormat = defaultFormatter.getDenominatorFormat();
        assertNotNull(denominatorFormat);
        assertTrue(denominatorFormat instanceof NumberFormat);
    }

    @Test
    public void testConstructorWithFormat() {
        NumberFormat format = NumberFormat.getInstance();
        ProperFractionFormat formatterWithFormat = new ProperFractionFormat(format);

        assertEquals(format, formatterWithFormat.getNumeratorFormat());
        assertEquals(format, formatterWithFormat.getDenominatorFormat());
    }

    @Test
    public void testConstructorWithWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat formatterWithWholeFormat = new ProperFractionFormat(wholeFormat, null, null);

        assertEquals(wholeFormat, formatterWithWholeFormat.getWholeFormat());
    }

    @Test
    public void testConstructorWithWholeFormatAndNumeratorFormatAndDenominatorFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();

        ProperFractionFormat formatterWithFormats = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        assertEquals(wholeFormat, formatterWithFormats.getWholeFormat());
        assertEquals(numeratorFormat, formatterWithFormats.getNumeratorFormat());
        assertEquals(denominatorFormat, formatterWithFormats.getDenominatorFormat());
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = this.formatter.format(fraction, buffer, pos);

        assertNotNull(result);
        assertEquals("3 / 4", result.toString());
    }

    @Test
    public void testFormatWithWhole() {
        Fraction fraction = new Fraction(5, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = this.formatter.format(fraction, buffer, pos);

        assertNotNull(result);
        assertEquals("1 1 / 4", result.toString());
    }

    @Test
    public void testFormatWithNegativeFraction() {
        Fraction fraction = new Fraction(-5, 4);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        StringBuffer result = this.formatter.format(fraction, buffer, pos);

        assertNotNull(result);
        assertEquals("-1 1 / 4", result.toString());
    }

    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        this.formatter.setWholeFormat(wholeFormat);

        NumberFormat result = this.formatter.getWholeFormat();

        assertNotNull(result);
        assertEquals(wholeFormat, result);
    }

    @Test
    public void testParse() throws ParseException {
        String source = "3 / 4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = this.formatter.parse(source, pos);

        assertNotNull(result);
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testParseWithWhole() throws ParseException {
        String source = "1 1 / 4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = this.formatter.parse(source, pos);

        assertNotNull(result);
        assertEquals(new Fraction(5, 4), result);
    }

    @Test
    public void testParseWithNegativeFraction() throws ParseException {
        String source = "-1 1 / 4";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = this.formatter.parse(source, pos);

        assertNotNull(result);
        assertEquals(new Fraction(-5, 4), result);
    }

    @Test
    public void testParseInvalidFormat() throws ParseException {
        String source = "3.5";
        ParsePosition pos = new ParsePosition(0);

        Fraction result = this.formatter.parse(source, pos);

        assertNull(result);
        assertEquals(0, pos.getIndex());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        this.formatter.setWholeFormat(wholeFormat);

        NumberFormat result = this.formatter.getWholeFormat();

        assertNotNull(result);
        assertEquals(wholeFormat, result);
    }
}