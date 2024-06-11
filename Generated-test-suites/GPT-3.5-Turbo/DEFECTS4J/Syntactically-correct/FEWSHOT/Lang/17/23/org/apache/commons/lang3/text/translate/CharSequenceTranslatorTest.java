package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CustomTranslator();
        assertEquals("abc", translator.translate("abc"));
        assertEquals("ABC", translator.translate("ABC"));
        assertEquals("abcdefABCDEF", translator.translate("abcdefABCDEF"));
        assertEquals("abcDEF", translator.translate("abcDEF"));
        assertNull(translator.translate(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();
        translator.translate("abc", null);
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("abc", 1, writer);
        assertEquals("bc", writer.toString());
        assertEquals(2, consumed);

        writer = new StringWriter();
        consumed = translator.translate("ABC", 1, writer);
        assertEquals("BC", writer.toString());
        assertEquals(2, consumed);

        writer = new StringWriter();
        consumed = translator.translate("abcdefABCDEF", 1, writer);
        assertEquals("bcdefABCDEF", writer.toString());
        assertEquals(2, consumed);

        writer = new StringWriter();
        consumed = translator.translate("abcDEF", 1, writer);
        assertEquals("bcDEF", writer.toString());
        assertEquals(2, consumed);
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();
        StringWriter writer = new StringWriter();
        int consumed = translator.translate("abc", 3, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);

        writer = new StringWriter();
        consumed = translator.translate("ABC", 3, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);

        writer = new StringWriter();
        consumed = translator.translate("abcdefABCDEF", 13, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);

        writer = new StringWriter();
        consumed = translator.translate("abcDEF", 6, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslateWithInvalidOutput() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();
        StringWriter writer = new StringWriter() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Invalid output");
            }
        };
        int consumed = translator.translate("abc", writer);
        assertEquals(-1, consumed);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("2A", CharSequenceTranslator.hex(42));
        assertEquals("7F", CharSequenceTranslator.hex(127));
    }

    private class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                return 0;
            }

            int len = Character.codePointCount(input, 0, input.length());
            if (index >= len) {
                return 0;
            }

            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return 1;
        }
    }
}