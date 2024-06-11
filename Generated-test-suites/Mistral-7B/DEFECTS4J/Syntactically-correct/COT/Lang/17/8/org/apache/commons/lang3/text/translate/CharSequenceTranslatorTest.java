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
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter();
        String input = "hello world";

        int consumed = translator.translate(input, 0, writer);
        assertEquals("h", writer.toString().substring(0, 1));
        consumed = translator.translate(input, consumed, writer);
        assertEquals("e", writer.toString().substring(1, 2));
        // ... continue testing the rest of the characters in the string

        assertEquals(input.length(), writer.toString().length());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateWithoutWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "hello world";
        String output = translator.translate(input);
        assertEquals("hello world", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     * Checks if exception is thrown when Writer is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "hello world";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     * Checks if exception is thrown when input is null.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, 0, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     * Checks if exception is thrown when input contains a surrogate pair.
     */
    @Test(expected = IOException.class)
    public void testTranslateWithSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "\uD834\uDF06\uD835\uDF07"; // Emoji: Grinning face with large eyes and smiling eyes
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "hello world";
        String output = aggregateTranslator.translate(input);
        // ... verify that the output is the result of translating the input by both translators
    }

}