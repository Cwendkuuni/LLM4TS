package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * JUnit 4 test file for the CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test basic functionality of translate method.
     */
    @Test
    public void testTranslateBasic() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, World!";
        String expected = "Hello, %20World%21";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test basic functionality of translate method with Writer.
     */
    @Test
    public void testTranslateWithWriterBasic() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = "Hello, %20World%21";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        translator.translate(null, null);
    }

    /**
     * Test null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, World!";
        translator.translate(input, null);
    }

    /**
     * Test empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "";
        String expected = "";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test edge case: translating an empty CharSequence with a Writer.
     */
    @Test
    public void testTranslateEmptyInputWithWriter() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.EMPTY, writer);
        String expected = "";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test exception handling: IOException.
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, World!";
        translator.translate(input, writer);
    }

    /**
     * Test merging multiple translators.
     */
    @Test
    public void testTranslateWithMergedTranslators() {
        CharSequenceTranslator firstTranslator = new DefaultTranslator();
        CharSequenceTranslator secondTranslator = new DefaultTranslator() {
            @Override
            public int translate(CharSequence cs, int index, Writer writer) throws IOException {
                return super.translate(cs, index, writer) + 1;
            }
        };
        CharSequenceTranslator mergedTranslator = new DefaultTranslator().with(firstTranslator, secondTranslator);
        String input = "Hello, World!";
        String expected = "Hello, %20World%21%01";
        String actual = mergedTranslator.translate(input);
        assertEquals(expected, actual);
    }

}