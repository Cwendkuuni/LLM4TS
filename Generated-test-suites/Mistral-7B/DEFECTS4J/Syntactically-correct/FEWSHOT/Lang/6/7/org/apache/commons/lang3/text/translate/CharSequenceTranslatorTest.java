package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate("A"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateWithWriterAndIOException() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        StringWriter writer = new StringWriter();
        assertThrows(RuntimeException.class, () -> translator.translate("ABC", writer));
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator translator = identity.with(hex);

        String input = "Hello, 世界!";
        assertEquals("48656C6C6F2C2075737465726963616C21", translator.translate(input).replaceAll("\\s", ""));
    }

    @Test
    public void testTranslateWithWriterAndAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator translator = identity.with(hex);

        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("48656C6C6F2C2075737465726963616C21", writer.toString().replaceAll("\\s", ""));
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        out.write(Character.toChars(Character.codePointAt(input, index)));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char codePoint = Character.codePointAt(input, index);
        String hex = CharSequenceTranslator.hex((int) codePoint);
        out.write(hex.toCharArray());
        return hex.length();
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
            index += consumed;
        }
        return consumed;
    }

}