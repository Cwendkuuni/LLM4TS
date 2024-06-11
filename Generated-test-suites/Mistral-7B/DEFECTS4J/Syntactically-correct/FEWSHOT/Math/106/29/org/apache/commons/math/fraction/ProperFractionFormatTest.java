package org.apache.commons.cli.test.math.fraction.properfractionformat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import org.apache.commons.cli.FractionFormat;
import org.apache.commons.cli.NumberFormatUtils;
import org.apache.commons.cli.ParsePosition;
import org.apache.commons.cli.WholeNumberFormat;
import org.apache.commons.cli.functions.NumberFormatFunction;
import org.apache.commons.cli.test.util.TestUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProperFractionFormatTest {

    private static final NumberFormat FUNCTION_FORMAT = NumberFormatFunction.getInstance(Locale.ENGLISH);

    @Test
    public void testProperFractionFormat() {
        // Create a ProperFractionFormat instance with default number formats
        FractionFormat format = new ProperFractionFormat();

        // Test formatting
        testFormat(format, new Fraction(3, 2), "3 / 2");
        testFormat(format, new Fraction(-3, 2), "-3 / 2");
        testFormat(format, new Fraction(3, 4), "3 / 4");
        testFormat(format, new Fraction(-3, 4), "-3 / 4");
        testFormat(format, new Fraction(1, 2), "1 / 2");
        testFormat(format, new Fraction(-1, 2), "-1 / 2");
        testFormat(format, new Fraction(0, 1), "0 / 1");
        testFormat(format, new Fraction(1, 1), "1");

        // Test formatting with custom number formats
        WholeNumberFormat wholeFormat = new WholeNumberFormat(NumberFormatUtils.DEFAULT);
        WholeNumberFormat numeratorFormat = new WholeNumberFormat(NumberFormatUtils.DEFAULT);
        WholeNumberFormat denominatorFormat = new WholeNumberFormat(NumberFormatUtils.DEFAULT);
        FractionFormat formatCustom = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test formatting
        testFormat(formatCustom, new Fraction(3, 2), "3 / 2");
        testFormat(formatCustom, new Fraction(-3, 2), "-3 / 2");
        testFormat(formatCustom, new Fraction(3, 4), "3 / 4");
        testFormat(formatCustom, new Fraction(-3, 4), "-3 / 4");
        testFormat(formatCustom, new Fraction(1, 2), "1 / 2");
        testFormat(formatCustom, new Fraction(-1, 2), "-1 / 2");
        testFormat(formatCustom, new Fraction(0, 1), "0 / 1");
        testFormat(formatCustom, new Fraction(1, 1), "1");

        // Test parsing
        testParse(format, "3 / 2", new Fraction(3, 2));
        testParse(format, "-3 / 2", new Fraction(-3, 2));
        testParse(format, "3 / 4", new Fraction(3, 4));
        testParse(format, "-3 / 4", new Fraction(-3, 4));
        testParse(format, "1 / 2", new Fraction(1, 2));
        testParse(format, "-1 / 2", new Fraction(-1, 2));
        testParse(format, "0 / 1", new Fraction(0, 1));
        testParse(format, "1", new Fraction(1, 1));

        // Test parsing with custom number formats
        testParse(formatCustom, "3 / 2", new Fraction(3, 2));
        testParse(formatCustom, "-3 / 2", new Fraction(-3, 2));
        testParse(formatCustom, "3 / 4", new Fraction(3, 4));
        testParse(formatCustom, "-3 / 4", new Fraction(-3, 4));
        testParse(formatCustom, "1 / 2", new Fraction(1, 2));
        testParse(formatCustom, "-1 / 2", new Fraction(-1, 2));
        testParse(formatCustom, "0 / 1", new Fraction(0, 1));
        testParse(formatCustom, "1", new Fraction(1, 1));

        // Test parsing invalid strings
        testParseError(format, "3 / 2 / 3", null);
        testParseError(format, "3 / 2 /", null);
        testParseError(format, "3 / 2/", null);
        testParseError(format, "3 -2 / 2", null);
        testParseError(format, "3 / 2 -", null);
        testParseError(format, "3 / 2/ -", null);
        testParseError(format, "3 / 2 / -", null);
        testParseError(format, "3 -2 / 2", null);
        testParseError(format, "3 / 2 -", null);
        testParseError(format, "3 / 2/ -", null);
        testParseError(format, "3 / 2 / -", null);
        testParseError(format, "3 2 / 2", null);
        testParseError(format, "3 2 /", null);
        testParseError(format, "3 2/", null);
        testParseError(format, "3 / 2 2", null);
        testParseError(format, "3 / 2 ", null);
        testParseError(format, "3 / 2", null);

        // Test parsing with custom number formats
        testParseError(formatCustom, "3 / 2 / 3", null);
        testParseError(formatCustom, "3 / 2 /", null);
        testParseError(formatCustom, "3 / 2/", null);
        testParseError(formatCustom, "3 -2 / 2", null);
        testParseError(formatCustom, "3 / 2 -", null);
        testParseError(formatCustom, "3 / 2/ -", null);
        testParseError(formatCustom, "3 / 2 / -", null);
        testParseError(formatCustom, "3 -2 / 2", null);
        testParseError(formatCustom, "3 / 2 -", null);
        testParseError(formatCustom, "3 / 2/ -", null);
        testParseError(formatCustom, "3 / 2 / -", null);
        testParseError(formatCustom, "3 2 / 2", null);
        testParseError(formatCustom, "3 2 /", null);
        testParseError(formatCustom, "3 2/", null);
        testParseError(formatCustom, "3 / 2 2", null);
        testParseError(formatCustom, "3 / 2 ", null);
        testParseError(formatCustom, "3 / 2", null);
    }

    private void testFormat(FractionFormat format, Fraction fraction, String expected) {
        String result = format.format(fraction, new StringBuffer(), null);
        assertEquals(expected, result.toString());
    }

    private void testParse(FractionFormat format, String input, Fraction expected) {
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(input, new ParsePosition(0));
        assertEquals(expected, parsed);
        assertEquals(input.length(), pos.getIndex());
    }

    private void testParseError(FractionFormat format, String input, Fraction expected) {
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(input, pos);
        assertNull(parsed);
        assertEquals(-1, pos.getIndex());
    }

    private void testParseError(FractionFormat format, String input) {
        testParseError(format, input, null);
    }

    private void testParseError(FractionFormat format, String input, Fraction expected, int offset) {
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());
        assertEquals(offset, pos.getErrorIndex());
    }

    private void testParseError(FractionFormat format, String input, Fraction expected, int offset, String expectedErrorMessage) {
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(input, pos);
        assertNull(parsed);
        assertEquals(input.length(), pos.getIndex());
        assertEquals(offset, pos.getErrorIndex());
        assertEquals(expectedErrorMessage, pos.getMessage());
    }

    private int parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        if (index >= source.length()) {
            return 0;
        }
        char c = source.charAt(index);
        pos.setIndex(index + 1);
        return c;
    }

    private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        while (Character.isWhitespace(parseNextCharacter(source, pos))) {
        }
    }

    private void testFormatCustomNumberFormats() {
        NumberFormat wholeFormat = FUNCTION_FORMAT;
        NumberFormat numeratorFormat = FUNCTION_FORMAT;
        NumberFormat denominatorFormat = FUNCTION_FORMAT;

        wholeFormat.setMinimumIntegerDigits(1);
        numeratorFormat.setMinimumIntegerDigits(1);
        denominatorFormat.setMinimumIntegerDigits(1);

        FractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        // Test formatting
        String input = "   3   /2  ";
        String expected = " 3 / 2";
        String result = format.format(new Fraction(3, 2), new StringBuffer(), null);
        assertEquals(expected, result.toString());
        JaroWinklerDistance jwd = new JaroWinklerDistance();
        double jaroDistance = jwd.distance(input, expected);
        assertEquals(0.96875, jaroDistance, 0.00001);

        // Test parsing
        ParsePosition pos = new ParsePosition(0);
        Fraction parsed = format.parse(input, pos);
        assertEquals(new Fraction(3, 2), parsed);
        assertEquals(input.length(), pos.getIndex());
    }
}