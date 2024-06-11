package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("22", CharSequenceTranslator.hex(0x22));
        assertEquals("00", CharSequenceTranslator.hex(0x00));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate("ABC"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ABCDEFG", translator.translate("ABCDEFG"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("ABC"), writer);
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("\uD800\uDC00"), writer);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(upperCase);
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("abc"), writer);
        assertEquals("ABC", writer.toString());
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (out != null && Character.isDefinedChar(Character.codePointAt(input, index))) {
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return c.length;
        }
        return 0;
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (out != null && Character.isDefinedChar(Character.codePointAt(input, index))) {
            char c = Character.codePointAt(input, index);
            out.write(Character.toUpperCase(c));
            return Character.charCount(c);
        }
        return 0;
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            if (consumed >= input.length()) {
                break;
            }
        }
        return consumed;
    }
}