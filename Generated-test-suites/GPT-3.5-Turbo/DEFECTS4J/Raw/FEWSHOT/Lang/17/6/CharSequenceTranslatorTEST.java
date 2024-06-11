package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslate_withSpecificTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('b');
                    return 1;
                }
                return 0;
            }
        };

        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("bbc", translator.translate("abc"));
        assertEquals("xyz", translator.translate("xyz"));
    }

    @Test
    public void testWith() {
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

        CharSequenceTranslator translator = firstTranslator.with(secondTranslator);

        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("7A", CharSequenceTranslator.hex(122));
    }
}