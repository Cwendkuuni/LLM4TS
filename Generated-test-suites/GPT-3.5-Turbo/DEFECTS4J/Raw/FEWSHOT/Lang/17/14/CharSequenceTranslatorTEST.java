package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    private static class TestTranslator extends CharSequenceTranslator {
        private final String input;
        private final String output;

        public TestTranslator(String input, String output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(this.output);
            return this.input.length();
        }
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator("abc", "123");
        assertEquals("123", translator.translate("abc"));
        assertNull(translator.translate(null));
    }
    
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator("abc", "123");
        Writer writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("123", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator("abc", "123");
        CharSequenceTranslator translator2 = new TestTranslator("123", "abc");
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertEquals("123", mergedTranslator.translate("abc"));
        assertEquals("abc", mergedTranslator.translate("123"));
    }

    @Test
    public void testHex() {
        assertEquals("20AC", CharSequenceTranslator.hex(8364));
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
}