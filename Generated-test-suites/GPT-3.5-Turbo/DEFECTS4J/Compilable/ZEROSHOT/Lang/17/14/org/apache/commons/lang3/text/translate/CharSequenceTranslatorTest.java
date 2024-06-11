package org.apache.commons.lang3.text.translate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNonNullInput_shouldReturnTranslatedString() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input";
        String expectedOutput = "Translated Test Input";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        assertNull(translator.translate(input));
    }

    @Test
    public void testTranslate_withZeroCodepointsConsumed_shouldOutputCodepoints() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expectedOutput = input;
        String actualOutput = writer.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslate_withNonZeroCodepointsConsumed_shouldSkipSurrogatePairs() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input \uD83D\uDE03";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expectedOutput = "Translated Test Input ";
        String actualOutput = writer.toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input";
        translator.translate(input, null);
    }

    @Test
    public void testWith_withSeveralTranslators_shouldReturnMergedTranslator() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex_withValidCodepoint_shouldReturnHexadecimalString() {
        int codepoint = 65;
        String expectedOutput = "41";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, actualOutput);
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (out == null) {
                throw new IllegalArgumentException("The Writer must not be null");
            }
            if (input == null) {
                return 0;
            }
            out.write("Translated ");
            out.write(input.toString());
            return input.length();
        }
    }
}