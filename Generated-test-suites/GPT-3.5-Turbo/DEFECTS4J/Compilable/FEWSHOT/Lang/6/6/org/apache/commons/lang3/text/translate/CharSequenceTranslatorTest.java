package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MyTranslator();

        assertEquals("ABC", translator.translate("abc"));
        assertEquals("123", translator.translate("123"));
        assertNull(translator.translate(null));
    }

    @Test(expected = IOException.class)
    public void testTranslate_withIOException() throws IOException {
        CharSequenceTranslator translator = new MyTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };

        translator.translate("abc");
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MyTranslator();
        CharSequenceTranslator translator2 = new MyTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("4D", CharSequenceTranslator.hex(77));
        assertEquals("7A", CharSequenceTranslator.hex(122));
        assertEquals("3A3A", CharSequenceTranslator.hex(149098));
        assertEquals("3B25", CharSequenceTranslator.hex(151973));
    }

    private static class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.toString().toUpperCase(Locale.ENGLISH));
            return input.length();
        }
    }
}