package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslationStrategy;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.IdentityTranslationStrategy;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * JUnit tests for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultTranslationStrategy();
        String input = "Hello World!";
        String expectedOutput = "48656c6c6f20576f726c6421";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslationStrategy();
        translator.translate(null, new StringWriter(10));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultTranslationStrategy();
        String output = translator.translate(null);
        assertEquals(null, output);
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new DefaultTranslationStrategy();
        String output = translator.translate(Character.toString(new char[0]));
        assertEquals("", output);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWriter() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslationStrategy();
        String input = "Hello World!";
        Writer out = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };
        translator.translate(input, out);
    }

    @Test
    public void testIdentityTranslationStrategy() {
        CharSequenceTranslator identityTranslator = new IdentityTranslationStrategy();
        String input = "Hello World!";
        String output = identityTranslator.translate(input);
        assertEquals(input, output);
    }

    @Test
    public void testCompositeTranslation() {
        CharSequenceTranslator identityTranslator = new IdentityTranslationStrategy();
        CharSequenceTranslator hexTranslator = new DefaultTranslationStrategy();
        CharSequenceTranslator compositeTranslator = identityTranslator.with(hexTranslator);
        String input = "Hello World!";
        String output = compositeTranslator.translate(input);
        String expectedOutput = "48656c6c6f20576f726c6421";
        assertEquals(expectedOutput, output);
    }
}