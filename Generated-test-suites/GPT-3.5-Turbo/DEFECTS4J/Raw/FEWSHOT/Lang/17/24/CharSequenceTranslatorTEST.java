
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {
    private class MockCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input);
            return input.length();
        }
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "test";
        String expected = "test";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithNull() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = null;
        String expected = null;
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "test";
        String expected = "test";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateToNullWriter() throws IOException {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "test";
        StringWriter writer = null;
        translator.translate(input, writer);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }

    @Test
    public void testHexWithLowerCase() {
        int codepoint = 97;
        String expected = "61";
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
}
