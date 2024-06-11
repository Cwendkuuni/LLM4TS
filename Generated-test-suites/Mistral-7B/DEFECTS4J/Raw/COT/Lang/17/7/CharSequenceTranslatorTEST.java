package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslateCharSequenceWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, 世界!";
        Writer out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals("Hello, \\u4E16\\u754C\\u4E22\\u4E00\\u597D!", out.toString());
        assertEquals(input.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateCharSequence() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, 世界!";
        String output = translator.translate(input);
        assertEquals("Hello, \\u4E16\\u754C\\u4E22\\u4E00\\u597D!", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        Writer out = new StringWriter();
        translator.translate(null, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello, 世界!";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null output
     */
    @Test
    public void testTranslateNullOutput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello, 世界!";
        assertNull(translator.translate(input));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with IOException
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello, 世界!";
        translator.translate(input, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
     */
    @Test
    public void testWithTranslators() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "Hello, 世界!";
        String output = mergedTranslator.translate(input);
        String expected = "Hello, \\u4E16\\u754C\\u4E22\\u4E00\\u597D!"; // default translator behavior
        assertEquals(expected, output);
    }

}