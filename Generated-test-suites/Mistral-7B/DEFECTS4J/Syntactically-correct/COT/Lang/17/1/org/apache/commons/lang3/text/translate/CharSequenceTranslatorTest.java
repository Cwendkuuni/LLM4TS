package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * JUnit 4 test file for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    // Test basic functionality of translate method
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "HelloWorld!";
        String expectedOutput = "HelloWorld!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    // Test exception handling when input is null
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        translator.translate(null);
    }

    // Test exception handling when out is null
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullOutput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "HelloWorld!";
        translator.translate(input, null);
    }

    // Test edge case: empty input
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "";
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    // Test edge case: input contains surrogate pairs
    @Test
    public void testTranslateSurrogatePairs() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "𠮷";
        String output = translator.translate(input);
        assertEquals("\uD840\uDC05", output);
    }

    // Test composite translator with multiple translators
    @Test
    public void testCompositeTranslator() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        String input = "HelloWorld!";
        String expectedOutput = "HelloWorld!";
        String output = compositeTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    // Test edge case: input is empty String and translator is a composite translator
    @Test
    public void testCompositeTranslatorEmptyInput() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        String input = "";
        String output = compositeTranslator.translate(input);
        assertNull(output);
    }

    // Test edge case: input is empty CharSequence and translator is a composite translator
    @Test
    public void testCompositeTranslatorEmptyCharSequence() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        CharSequence input = Character.TO_CHAR_ARRAY_BUFFER;
        int index = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(outputStream);
        int actualConsumed = compositeTranslator.translate(input, index, out);
        assertEquals(0, actualConsumed);
        assertNull(outputStream.toString());
    }

    // Test exception handling when Writer throws an IOException
    @Test(expected = RuntimeException.class)
    public void testWriterIOException() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "HelloWorld!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(outputStream) {
            @Override
            public void write(int c, int start, int length) throws IOException {
                throw new IOException();
            }
        };
        int actualConsumed = translator.translate(input, 0, out);
    }

}