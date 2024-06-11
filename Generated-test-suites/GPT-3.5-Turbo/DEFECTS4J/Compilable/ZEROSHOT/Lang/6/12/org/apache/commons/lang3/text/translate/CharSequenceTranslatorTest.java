package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
    }

    @Test
    public void testTranslate() {
        String input = "test";
        assertEquals(input, translator.translate(input));
    }

    @Test
    public void testTranslate_NullInput() {
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_IOExceptionThrown() {
        CharSequenceTranslator throwableTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        String input = "test";
        assertNotNull(throwableTranslator.translate(input));
    }

    @Test
    public void testTranslate_WriterNull() {
        CharSequenceTranslator nullWriterTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String input = "test";
        assertNotNull(nullWriterTranslator.translate(input));
    }

    @Test
    public void testTranslate_WithTranslator() {
        CharSequenceTranslator anotherTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        CharSequenceTranslator mergedTranslator = translator.with(anotherTranslator);
        String input = "test";
        assertEquals(input, mergedTranslator.translate(input));
    }

    @Test
    public void testTranslate_WithTranslators() {
        CharSequenceTranslator firstTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        CharSequenceTranslator secondTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        CharSequenceTranslator[] translators = {firstTranslator, secondTranslator};
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        String input = "test";
        assertEquals(input, mergedTranslator.translate(input));
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    @Test
    public void testHex_LowerCase() {
        int codepoint = 97;
        String expected = "61";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

}