package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_withNonEmptyInputContainingNoSpecialCharacters_shouldReturnInputUnchanged() {
        String input = "This is a regular sentence.";
        assertEquals(input, translator.translate(input));
    }

    @Test
    public void testTranslate_withSpecialCharacters_shouldEscapeThem() {
        String input = "& < > ' \"";
        String expectedOutput = "&amp; &lt; &gt; &apos; &quot;";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslate_withInvalidWriter_shouldThrowException() {
        try {
            translator.translate("Test", new TestWriter());
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Test passed
        }
    }

    @Test
    public void testTranslate_withValidWriter_shouldTranslateSuccessfully() {
        StringWriter writer = new StringWriter();
        try {
            translator.translate("Test", writer);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals("Translated Test", writer.toString());
    }

    @Test
    public void testWith_withAdditionalTranslators_shouldTranslateSuccessfully() {
        CharSequenceTranslator additionalTranslator = new TestTranslator2();
        CharSequenceTranslator mergedTranslator = translator.with(additionalTranslator);

        String input = "Test";
        String expectedOutput = "Translated Twice";
        assertEquals(expectedOutput.concat(" and Translated Twice"), mergedTranslator.translate(input));
    }

    @Test
    public void testHex_withValidCodepoint_shouldReturnUpperCaseHexadecimalValue() {
        int codepoint = 65; // ASCII value of 'A'
        String expectedOutput = "41";
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                out.write("Translated ");
                out.write(input.toString().substring(index));
                return input.length() - index;
            } else {
                return 0;
            }
        }
    }

    private static class TestTranslator2 extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                out.write("Twice");
                return input.length() - index;
            } else {
                return 0;
            }
        }
    }

    private static class TestWriter extends Writer {

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }

        @Override
        public void flush() throws IOException {
            // Not implemented for the test
        }

        @Override
        public void close() throws IOException {
            // Not implemented for the test
        }
    }
}