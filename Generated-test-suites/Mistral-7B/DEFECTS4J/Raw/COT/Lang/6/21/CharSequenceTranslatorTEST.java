package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with empty CharSequence input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast("")));
    }

    /**
     * Test translate method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        translator.translate(null);
    }

    /**
     * Test translate method with basic input.
     */
    @Test
    public void testTranslateBasicInput() {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        String expected = "expected";
        String actual = translator.translate(CharSequence.class.cast(expected));
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with Writer output.
     */
    @Test
    public void testTranslateWriterOutput() throws IOException {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "input";
        int consumed = translator.translate(input, 0, writer);
        String actual = writer.toString();
        String expected = "expected";
        assertEquals(expected, actual.substring(0, consumed));
    }

    /**
     * Test translate method with surrogate pair.
     */
    @Test
    public void testTranslateSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "😃";
        int consumed = translator.translate(input, 0, writer);
        String actual = writer.toString();
        String expected = "grinning face with smiling eyes";
        assertEquals(expected, actual.substring(0, consumed));
    }

    /**
     * Test translate method with multiple codepoints.
     */
    @Test
    public void testTranslateMultipleCodepoints() throws IOException {
        CharSequenceTranslator translator = new StubCharSequenceTranslator();
        StringWriter writer = new StringWriter();
        String input = "hello world";
        int consumed = translator.translate(input, 0, writer);
        String actual = writer.toString();
        String expected = "hello world";
        assertEquals(expected, actual.substring(0, consumed));
    }

    /**
     * Test translate method with exception.
     */
    @Test(expected = IOException.class)
    public void testTranslateException() throws IOException {
        CharSequenceTranslator translator = new IOExceptionTranslator();
        StringWriter writer = new StringWriter();
        String input = "input";
        translator.translate(input, 0, writer);
    }

    /**
     * Test translate method with multiple translators.
     */
    @Test
    public void testTranslateMultipleTranslators() throws IOException {
        CharSequenceTranslator translator1 = new StubCharSequenceTranslator();
        CharSequenceTranslator translator2 = new StubCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});
        StringWriter writer = new StringWriter();
        String input = "input";
        int consumed = mergedTranslator.translate(input, 0, writer);
        String actual = writer.toString();
        String expected = "expected"; // Note: You need to implement the logic for the merged translators
        assertEquals(expected, actual.substring(0, consumed));
    }

}

class StubCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }
}

class IOExceptionTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException();
    }
}