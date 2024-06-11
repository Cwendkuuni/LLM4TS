package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        assertEquals("abc", new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('a');
                return 1;
            }
        }.translate("def"));

        assertEquals("def", new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        }.translate("def"));

        assertNull(new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        }.translate(null));
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('a');
                return 1;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('b');
                return 1;
            }
        };

        assertEquals("ab", translator1.with(translator2).translate("def"));
        assertEquals("def", translator2.with(translator1).translate("def"));
        assertEquals("abc", translator1.with(translator2).translate("defabc"));
        assertEquals("bc", translator2.with(translator1).translate("defabc"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
        assertEquals("30", CharSequenceTranslator.hex(48));
        assertEquals("31", CharSequenceTranslator.hex(49));
        assertEquals("32", CharSequenceTranslator.hex(50));
    }
}