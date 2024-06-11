package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("25", CharSequenceTranslator.hex(35));
        assertEquals("0A", CharSequenceTranslator.hex(10));
    }

    @Test
    public void testTranslateNull() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmpty() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast(new StringBuilder())));
    }

    @Test
    public void testTranslateSimple() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("abc", translator.translate(CharSequence.class.cast("abc")));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("abc"), 0, writer);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslateSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = "𠮷";
        translator.translate(input, 0, writer);
        assertEquals("\uD840\uDC07", writer.toString());
    }

    @Test
    public void testTranslateAggregate() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = (cs, index, out) -> {
            for (int cp = Character.codePointAt(cs, index); cp > 0; cp = Character.codePointAt(cs, index + Character.charCount(cs, index))) {
                out.write(CharSequenceTranslator.hex(cp).getBytes());
                index += Character.charCount(cs, index);
            }
        };
        CharSequenceTranslator translator = identity.with(hex);
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("abc\uD840\uDC07"), 0, writer);
        assertEquals("61626125D840DC07", writer.toString());
    }

}

class IdentityTranslator implements CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(input, index);
    }

}