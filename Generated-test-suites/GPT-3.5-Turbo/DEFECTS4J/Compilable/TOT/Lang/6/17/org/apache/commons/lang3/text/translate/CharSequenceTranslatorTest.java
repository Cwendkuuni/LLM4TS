package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateWithValidInput() {
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals("123", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new TestTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("IOException");
            }
        };
        try {
            translator.translate("abc");
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("IOException", e.getCause().getMessage());
        }
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("123");
            return 0;
        }
    }
}