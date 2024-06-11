package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };

    @Test
    public void testTranslateWithNullInput() {
        assertNull(translator.translate((CharSequence) null));
    }

    @Test
    public void testTranslateWithNullWriter() {
        try {
            translator.translate("test", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithEmptyInput() {
        try {
            StringWriter writer = new StringWriter();
            translator.translate("", writer);
            assertEquals("", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithNonEmptyInput() {
        try {
            StringWriter writer = new StringWriter();
            translator.translate("test", writer);
            assertEquals("test", writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithCustomTranslators() {
        CharSequenceTranslator[] translators = { mock(CharSequenceTranslator.class) };
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        assertEquals(mergedTranslator.getClass(), AggregateTranslator.class);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65)); // 'A'
        assertEquals("7A", CharSequenceTranslator.hex(122)); // 'z'
        assertEquals("31", CharSequenceTranslator.hex(49)); // '1'
    }

    @Test
    public void testTranslateIndexWithNullOut() {
        CharSequence input = "test";
        int index = 0;
        try {
            translator.translate(input, index, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("test", input.toString());
    }
}