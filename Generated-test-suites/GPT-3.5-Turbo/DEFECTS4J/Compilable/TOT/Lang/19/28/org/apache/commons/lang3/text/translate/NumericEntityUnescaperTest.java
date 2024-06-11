package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_WithValidHexadecimalInput_ShouldReturnCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x0041;"; // A

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(7, numCharsWritten);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WithValidDecimalInput_ShouldReturnCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;"; // A

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(6, numCharsWritten);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WithMissingSemicolon_ShouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65"; // Missing semicolon

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(0, numCharsWritten);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidHexadecimalInput_ShouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#xG;"; // Invalid hexadecimal value

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(0, numCharsWritten);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidDecimalInput_ShouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#ABC;"; // Invalid decimal value

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(0, numCharsWritten);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidSequence_ShouldReturnZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&amp;"; // Invalid sequence

        int numCharsWritten = unescaper.translate(input, 0, writer);

        assertEquals(0, numCharsWritten);
        assertEquals("", writer.toString());
    }
}

