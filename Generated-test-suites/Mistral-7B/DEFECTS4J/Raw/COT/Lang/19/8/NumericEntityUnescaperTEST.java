package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with valid inputs
     */
    @Test
    public void testTranslateValidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#x1F601;&#128516;&#x1F602;";
        int index = 0;

        int actualLength = unescaper.translate(new String(input), index, out);
        String expectedOutput = "\uD83D\uDC65\uD83D\uDC26\uD83D\uDC66";
        String output = new String(out.toByteArray(), StandardCharsets.UTF_8);

        assertEquals(expectedOutput.length(), actualLength);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - no & character
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNoAmpersand() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "1234";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - no &# character
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNoHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&1234";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - no ; character
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#[123";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - hex value out of range
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidHexValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#[xFFFFF]";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - decimal value out of range
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidDecimalValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#[2147483649]";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - invalid hex character
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#[xAA]";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     * with invalid input - mixed hex and decimal
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMixedHexDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new ByteArrayOutputStream(10);

        String input = "&#[x1F6 ABC]";
        int index = 0;

        unescaper.translate(new String(input), index, out);
    }
}