package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("22", CharSequenceTranslator.hex(0x22));
        assertEquals("04", CharSequenceTranslator.hex(0x04));
        assertEquals("00", CharSequenceTranslator.hex(0x00));
        assertEquals("FF", CharSequenceTranslator.hex(0xFF));
    }

    @Test
    public void testTranslateEmpty() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(Character.toString(new char[0])));
    }

    @Test
    public void testTranslateSingleCharacter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate("A"));
    }

    @Test
    public void testTranslateSingleCharacterToWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream(1));
        translator.translate("A", out);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateMultipleCharacters() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testTranslateMultipleCharactersToWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream(4));
        translator.translate("ABC", out);
        assertEquals("ABC", out.toString());
    }

    @Test
    public void testTranslateSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream(6));
        String input = "𠮷";
        translator.translate(input, out);
        assertEquals("\uD840\uDC07", out.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new UpperCaseTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        Writer out = new OutputStreamWriter(new ByteArrayOutputStream(5));
        String input = "AbC";
        compositeTranslator.translate(input, out);
        assertEquals("ABC", out.toString());

        assertEquals("ABC", compositeTranslator.translate(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("A", null);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(Character.toString(new char[0])));
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence cs, int index, Writer out) throws IOException {
            if (index < 0 || index >= cs.length()) {
                throw new IndexOutOfBoundsException();
            }
            out.write(Character.toString(cs.charAt(index)));
            return Character.length(cs.charAt(index));
        }

        @Override
        public String translate(CharSequence cs) {
            return cs.toString();
        }
    }

    private static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence cs, int index, Writer out) throws IOException {
            char c = Character.toUpperCase(cs.charAt(index));
            out.write(Character.toString(c));
            return Character.length(c);
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence cs, int index, Writer out) throws IOException {
            int length = 0;
            for (CharSequenceTranslator translator : translators) {
                int translatedLength = translator.translate(cs, index, out);
                length += translatedLength;
                index += translatedLength;
                if (length >= cs.length()) {
                    break;
                }
            }
            return length;
        }

        @Override
        public String translate(CharSequence cs) {
            StringBuilder builder = new StringBuilder();
            for (CharSequenceTranslator translator : translators) {
                builder.append(translator.translate(cs));
            }
            return builder.toString();
        }
    }
}