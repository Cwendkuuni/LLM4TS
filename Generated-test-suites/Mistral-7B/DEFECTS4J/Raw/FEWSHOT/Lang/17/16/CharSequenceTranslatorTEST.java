package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    /**
     * Test for abstract method translate
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "This is a test";
        StringWriter output = new StringWriter();

        int consumed = translator.translate(input, 0, output);
        assertEquals("Expecting some codepoints to be translated", consumed, 13);
        assertEquals("Output should match expected translation", translator.translate(input), output.toString());
    }

    /**
     * Test for translate method with empty input
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "";
        StringWriter output = new StringWriter();

        translator.translate(input, 0, output);
        assertNull("Output should be empty for empty input", output.toString());
    }

    /**
     * Test for translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = null;
        StringWriter output = null;

        translator.translate(input, 0, output);
    }

    /**
     * Test for translate method with null output
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullOutput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "This is a test";
        Writer output = null;

        translator.translate(input, 0, output);
    }

    /**
     * Test for translate method with IOException
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslatorWithIOException();
        String input = "This is a test";
        StringWriter output = new StringWriter();

        translator.translate(input, 0, output);
    }

    /**
     * Test for translate method with AggregateTranslator
     */
    @Test
    public void testTranslateAggregateTranslator() {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "This is a test";
        StringWriter output = new StringWriter();

        int consumed = aggregateTranslator.translate(input, 0, output);
        assertEquals("Expecting all codepoints to be translated", consumed, 13);
        assertEquals("Output should match expected translation with both translators",
                aggregateTranslator.translate(input), output.toString());
    }

    /**
     * Test for hex method
     */
    @Test
    public void testHex() {
        assertEquals("0x31 0x35 0x20 0x69 0x6e 0x66 0x6f 0x73 0x74 0x20 0x61 0x73 0x74 0x75 0x73",
                CharSequenceTranslator.hex(0x313520696E666F7374206173747573));
    }
}

class MyCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write(c);
        return Character.charCount(c);
    }
}

class MyCharSequenceTranslatorWithIOException extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException("Simulated IOException");
    }
}