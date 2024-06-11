package org.apache.commons.lang3.text.translate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // Dummy translation method for testing purposes
            return 0;
        }
    };

    @Test
    public void testTranslate_NullInput() {
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_EmptyInput() {
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslate_ValidInput() {
        String input = "Hello, World!";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    @Test
    public void testTranslate_CharSequenceWriter_NullWriter() {
        String input = "Hello, World!";
        try {
            translator.translate(input, null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    public void testTranslate_CharSequenceWriter_NullInput() {
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, writer);
            String result = writer.toString();
            assertNull(result);
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    public void testTranslate_CharSequenceWriter_EmptyInput() {
        StringWriter writer = new StringWriter();
        String input = "";
        try {
            translator.translate(input, writer);
            String result = writer.toString();
            assertEquals(input, result);
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    public void testTranslate_CharSequenceWriter_ValidInput() {
        StringWriter writer = new StringWriter();
        String input = "Hello, World!";
        try {
            translator.translate(input, writer);
            String result = writer.toString();
            assertEquals(input, result);
        } catch (IOException e) {
            // Ignore
        }
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("5A", CharSequenceTranslator.hex(90));
        assertEquals("61", CharSequenceTranslator.hex(97));
    }

}