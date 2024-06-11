package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    private static final CharSequenceTranslator TRANSLATOR = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(String.valueOf(input.charAt(index)));
            return 1;
        }
    };

    @Test
    public void testTranslate_emptyString() {
        String result = TRANSLATOR.translate("");
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testTranslate_singleCharacter() {
        String result = TRANSLATOR.translate("a");
        assertNotNull(result);
        assertEquals("a", result);
    }

    @Test
    public void testTranslate_multipleCharacters() {
        String result = TRANSLATOR.translate("abcd");
        assertNotNull(result);
        assertEquals("abcd", result);
    }

    @Test
    public void testTranslate_unicodeCharacters() {
        String result = TRANSLATOR.translate("©∞ÅÇ");
        assertNotNull(result);
        assertEquals("©∞ÅÇ", result);
    }

    @Test
    public void testTranslate_nullInput() {
        String result = TRANSLATOR.translate(null);
        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testTranslate_writer_emptyString() throws IOException {
        StringWriter writer = new StringWriter();
        TRANSLATOR.translate("", writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    public void testTranslate_writer_singleCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        TRANSLATOR.translate("a", writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("a", result);
    }

    @Test
    public void testTranslate_writer_multipleCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        TRANSLATOR.translate("abcd", writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("abcd", result);
    }

    @Test
    public void testTranslate_writer_unicodeCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        TRANSLATOR.translate("©∞ÅÇ", writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("©∞ÅÇ", result);
    }

    @Test
    public void testTranslate_writer_nullInput() throws IOException {
        StringWriter writer = new StringWriter();
        TRANSLATOR.translate(null, writer);
        String result = writer.toString();
        assertNotNull(result);
        assertEquals("null", result);
    }

    @Test
    public void testHex() {
        String result = CharSequenceTranslator.hex(65);
        assertNotNull(result);
        assertEquals("41", result);
    }

    @Test
    public void testHex_negativeValue() {
        String result = CharSequenceTranslator.hex(-65);
        assertNotNull(result);
        assertEquals("FFFFFFBF", result);
    }

    @Test
    public void testHex_zeroValue() {
        String result = CharSequenceTranslator.hex(0);
        assertNotNull(result);
        assertEquals("0", result);
    }

    @Test
    public void testHex_unicodeCharacter() {
        String result = CharSequenceTranslator.hex(169);
        assertNotNull(result);
        assertEquals("A9", result);
    }
}