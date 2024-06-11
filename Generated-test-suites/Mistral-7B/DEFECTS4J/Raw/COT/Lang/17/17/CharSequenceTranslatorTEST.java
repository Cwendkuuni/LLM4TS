package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        String expected = "test";
        StringWriter output = new StringWriter();
        int consumed = translator.translate(input, 0, output);
        assertEquals("Expected same string", expected, output.toString());
        assertEquals("Expected same number of codepoints consumed", input.length(), consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateToWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        String expected = "test";
        String output = translator.translate(input);
        assertEquals("Expected same string", expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with Writer argument as null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with negative index
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithNegativeIndex() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        StringWriter output = new StringWriter();
        translator.translate(input, -1, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with CharSequence argument as null
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullCharSequence() {
        DefaultTranslator translator = new DefaultTranslator();
        StringWriter output = new StringWriter();
        translator.translate(null, 0, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with CharSequence argument as empty
     */
    @Test
    public void testTranslateWithEmptyCharSequence() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "";
        StringWriter output = new StringWriter();
        int consumed = translator.translate(input, 0, output);
        assertEquals("Expected empty string", "", output.toString());
        assertEquals("Expected zero codepoints consumed", 0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with Writer argument as StringWriter
     */
    @Test
    public void testTranslateToStringWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter output = new OutputStreamWriter(outputStream);
        int consumed = translator.translate(input, 0, output);
        String expected = "test";
        String actual = new String(outputStream.toByteArray());
        assertEquals("Expected same string", expected, actual);
        assertEquals("Expected same number of codepoints consumed", input.length(), consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with Writer not able to write
     */
    @Test(expected = IOException.class)
    public void testTranslateWithIOExceptionWriter() throws IOException {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        StringWriter output = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Unable to write");
            }
        };
        int consumed = translator.translate(input, 0, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}
     */
    @Test
    public void testWith() {
        DefaultTranslator first = new DefaultTranslator();
        DefaultTranslator second = new DefaultTranslator();
        CharSequenceTranslator result = first.with(second);
        assertEquals("Expected merged translator", first.translate("test"), result.translate("test"));
    }

    /**
     * Test method for {@link CharSequenceTranslator#hex(int)}
     */
    @Test
    public void testHex() {
        assertEquals("Expected '41' for 'A'", "41", CharSequenceTranslator.hex(65));
        assertEquals("Expected '25' for '%'", "25", CharSequenceTranslator.hex(37));
    }
}