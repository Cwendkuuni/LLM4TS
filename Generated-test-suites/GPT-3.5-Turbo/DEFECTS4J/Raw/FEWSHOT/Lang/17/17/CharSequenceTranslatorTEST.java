package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        assertEquals("abc", CharSequenceTranslator.translate("abc"));
        assertEquals("", CharSequenceTranslator.translate(null));        
    }

    @Test
    public void testTranslateWithIndexAndWriter() {
        Writer writer = new StringWriter();
        assertEquals(3, CharSequenceTranslator.translate("abc", 0, writer));
        assertEquals("abc", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, CharSequenceTranslator.translate(null, 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0;
            }
        };
        assertEquals(translator, CharSequenceTranslator.with(translator));
        
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0;
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) {
                return 0;
            }
        };
        assertEquals(translator1, CharSequenceTranslator.with(translator1, translator2));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
    }
}