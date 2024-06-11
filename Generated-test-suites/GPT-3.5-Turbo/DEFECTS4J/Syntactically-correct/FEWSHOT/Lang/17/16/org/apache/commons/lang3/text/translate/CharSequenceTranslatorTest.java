package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(String.valueOf(input.charAt(index)));
                return 1;
            }
        };

        assertEquals("ABC", translator.translate("ABC"));
        assertEquals("DEF", translator.translate("DEF"));
        assertEquals("XYZ", translator.translate("XYZ"));
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_Exception() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };

        assertThrows(RuntimeException.class, () -> {
            translator.translate("ABC");
        });
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(String.valueOf(input.charAt(index)));
                return 1;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(String.valueOf(input.charAt(index)).toUpperCase(Locale.ENGLISH));
                return 1;
            }
        };

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assertEquals("A", mergedTranslator.translate("a"));
        assertEquals("B", mergedTranslator.translate("b"));
        assertEquals("C", mergedTranslator.translate("c"));
        assertEquals("AB", mergedTranslator.translate("ab"));
        assertEquals("BC", mergedTranslator.translate("bc"));
        assertEquals("CD", mergedTranslator.translate("cd"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex('A'));
        assertEquals("42", CharSequenceTranslator.hex('B'));
        assertEquals("43", CharSequenceTranslator.hex('C'));
        assertEquals("44", CharSequenceTranslator.hex('D'));
    }

}