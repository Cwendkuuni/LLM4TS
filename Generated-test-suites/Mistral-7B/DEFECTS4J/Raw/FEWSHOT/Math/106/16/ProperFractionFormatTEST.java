package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.DecimalFormatSymbols;
import org.apache.commons.math3.util.MathUtils;

import org.apache.commons.cli.ProperFractionFormat;

public class ProperFractionFormatTest {

    @Test
    public void testDefaultFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat wholeFormat = format.getWholeFormat();
        NumberFormat numeratorFormat = format.getNumeratorFormat();
        NumberFormat denominatorFormat = format.getDenominatorFormat();

        assertEquals(NumberFormat.getDefault(), wholeFormat);
        assertEquals(NumberFormat.getDefault(), numeratorFormat);
        assertEquals(NumberFormat.getDefault(), denominatorFormat);

        // Test formatting
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        String expected = "1/2";
        assertEquals(expected, toAppendTo.toString());

        // Test parsing
        ParsePosition posParse = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1/2", posParse);
        assertEquals(fraction, parsedFraction);
        assertEquals(1, posParse.getIndex());
        assertEquals(-1, posParse.getErrorIndex());

        // Test parsing with non-default locale
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRENCH);
        NumberFormat frenchFormat = new DecimalFormat("#,##0.# %", symbols);
        ProperFractionFormat frenchFormat = new ProperFractionFormat(frenchFormat,
                frenchFormat, frenchFormat);
        Fraction frenchFraction = new Fraction(1, 3);
        posParse.setIndex(0);
        Fraction parsedFrenchFraction = frenchFormat.parse("1/3", posParse);
        assertEquals(frenchFraction, parsedFrenchFraction);
        assertEquals(3, posParse.getIndex());
        assertEquals(-1, posParse.getErrorIndex());
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat numeratorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat denominatorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test formatting
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        String expected = "1, / 2";
        assertEquals(expected, toAppendTo.toString());

        // Test parsing
        ParsePosition posParse = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1, / 2", posParse);
        assertEquals(fraction, parsedFraction);
        assertEquals(3, posParse.getIndex());
        assertEquals(-1, posParse.getErrorIndex());
    }

    @Test
    public void testParseWholeWithSign() {
        NumberFormat wholeFormat = new DecimalFormat("#,-#", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat numeratorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat denominatorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = format.parse(source, pos);
        assertEquals(new Fraction(-1, 1, 2), parsedFraction);
        assertEquals(3, pos.getIndex());
        assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testParseInvalidFormat() {
        NumberFormat wholeFormat = new DecimalFormat("###", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat numeratorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat denominatorFormat = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "-1 -1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }

    @Test
    public void testParseInvalidNumberFormat() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0", null);
        NumberFormat numeratorFormat = new DecimalFormat("#,##0", null);
        NumberFormat denominatorFormat = new DecimalFormat("#,##0", null);

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        String source = "1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction parsedFraction = format.parse(source, pos);
        assertNull(parsedFraction);
        assertEquals(0, pos.getIndex());
        assertEquals(-1, pos.getErrorIndex());
    }

    @Test
    public void testFormatWithBigDecimal() {
        NumberFormat wholeFormat = new DecimalFormat("#,##0.# %", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat numeratorFormat = new DecimalFormat("#,##0.# %", DecimalFormatSymbols.getInstance(Locale.US));
        NumberFormat denominatorFormat = new DecimalFormat("#,##0.# %", DecimalFormatSymbols.getInstance(Locale.US));

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(new BigDecimal("1.5"), new BigDecimal("2.5"));
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0, 0);
        format.format(fraction, toAppendTo, pos);
        String expected = "1, 1/2";
        assertEquals(expected, toAppendTo.toString());

        ParsePosition posParse = new ParsePosition(0);
        Fraction parsedFraction = format.parse("1, 1/2", posParse);
        assertEquals(fraction, parsedFraction);
        assertEquals(5, posParse.getIndex());
        assertEquals(-1, posParse.getErrorIndex());
    }
}