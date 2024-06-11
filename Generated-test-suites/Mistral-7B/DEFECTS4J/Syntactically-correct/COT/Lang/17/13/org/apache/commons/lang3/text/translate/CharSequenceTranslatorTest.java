package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.IOExceptionTranslator;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test file for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;
    private CharSequenceTranslator ioExceptionTranslator;
    private CharSequenceTranslator aggregateTranslator;

    @Before
    public void setUp() {
        translator = new IOEAsciiLowerCaseTranslator(); // Assuming you have a valid implementation of IOEAsciiLowerCaseTranslator
        ioExceptionTranslator = new IOExceptionTranslator();
        aggregateTranslator = translator.with(ioExceptionTranslator);
    }

    @Test
    public void testTranslateNormalCase() {
        String input = "Hello World!";
        String expected = "hello world!";

        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        String input = "Hello World!";

        Writer out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        String output = out.toString();

        assertEquals("hello world!", output);
        assertEquals(input.length() * 2, output.length());
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        String input = "Hello World!";
        String expected = "hello world!";

        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        String input = "Hello World!";

        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testIOExceptionTranslator() {
        String input = "\uD840\uDC00\uD840\uDC01"; // Surrogate pair
        StringWriter outputWriter = new StringWriter();
        Writer output = outputWriter;

        assertThrows(IOException.class, () -> ioExceptionTranslator.translate(input, 0, output));
    }

    @Test
    public void testAggregateTranslator() {
        String input = "Hello World!IOError";
        String expected = "hello world!IOError";

        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }
}