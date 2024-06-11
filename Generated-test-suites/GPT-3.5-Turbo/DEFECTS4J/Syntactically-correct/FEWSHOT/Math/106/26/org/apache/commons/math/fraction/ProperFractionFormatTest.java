
package org.apache.commons.math.fraction;

import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class ProperFractionFormatTest {

    private ProperFractionFormat format;

    public ProperFractionFormatTest() {
        format = new ProperFractionFormat();
    }

    @Test
    public void testFormat() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(-1, 2);
        Fraction fraction3 = new Fraction(5, 1);

        String result1 = format.format(fraction1);
        String result2 = format.format(fraction2);
        String result3 = format.format(fraction3);

        assertEquals("2 / 3", result1);
        assertEquals("-1 / 2", result2);
        assertEquals("5", result3);
    }

    @Test
    public void testParse() throws ParseException {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(-2, 5);
        Fraction fraction3 = new Fraction(1, 1);

        Fraction result1 = format.parse("3 / 4");
        Fraction result2 = format.parse("-2 / 5");
        Fraction result3 = format.parse("1");

        assertEquals(fraction1, result1);
        assertEquals(fraction2, result2);
        assertEquals(fraction3, result3);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalid() throws ParseException {
        format.parse("-2 / -5");
    }

    @Test
    public void testParseLeadingWhitespace() throws ParseException {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = format.parse(" 3 / 4");

        assertEquals(fraction, result);
    }

    @Test
    public void testParseTrailingWhitespace() throws ParseException {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = format.parse("3 / 4 ");

        assertEquals(fraction, result);
    }

    @Test
    public void testParseWhitespaceInBetween() throws ParseException {
        Fraction fraction = new Fraction(3, 4);
        Fraction result = format.parse("3   / 4");

        assertEquals(fraction, result);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidCharacter() throws ParseException {
        format.parse("3 / 4 /");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidWhitespace() throws ParseException {
        format.parse("3/ 4");
    }

    @Test
    public void testWholeNumberFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        ProperFractionFormat format = new ProperFractionFormat(wholeFormat);

        Fraction fraction = new Fraction(5, 1);

        String result = format.format(fraction);

        assertEquals("5", result);
    }

    @Test
    public void testCustomFormat() {
        NumberFormat wholeFormat = NumberFormat.getInstance();
        NumberFormat numeratorFormat = NumberFormat.getInstance();
        NumberFormat denominatorFormat = NumberFormat.getInstance();

        ProperFractionFormat format = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);

        Fraction fraction = new Fraction(2, 3);
        wholeFormat.setMaximumIntegerDigits(1);
        numeratorFormat.setMinimumIntegerDigits(3);
        denominatorFormat.setMinimumIntegerDigits(3);

        String result = format.format(fraction);

        assertEquals("2    / 003", result);
    }
}
