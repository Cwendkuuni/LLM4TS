package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "hello world";
        String expected = "48656c6c6f20776f726c64";
        StringWriter output = new StringWriter();
        int index = 0;

        int result = translator.translate(input, index, output);
        assertEquals(expected, output.toString());
        assertEquals(input.length() * 2, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslate_String() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "hello world";
        String expected = "48656c6c6f20776f726c64";

        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        Writer out = new StringWriter();

        translator.translate(null, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "hello world";

        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input
     */
    @Test
    public void testTranslate_EmptyInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        StringWriter output = new StringWriter();
        CharSequence input = "";
        int index = 0;

        int result = translator.translate(input, index, output);
        assertEquals("", output.toString());
        assertEquals(0, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with surrogate pair
     */
    @Test
    public void testTranslate_SurrogatePair() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "𠮷";
        StringWriter output = new StringWriter();

        translator.translate(input, output);
        assertEquals("d840\u0dcf", output.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator translator3 = new DefaultTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);

        assertEquals(4, mergedTranslator.getClass().getDeclaredFields().length);
    }
}