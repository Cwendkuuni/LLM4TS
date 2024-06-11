package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("26", CharSequenceTranslator.hex(38));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("61", CharSequenceTranslator.hex(97));
        assertEquals("2772", CharSequenceTranslator.hex(11562));
    }

    @Test
    public void testTranslateNullInput() {
        assertNull(CharSequenceTranslator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        assertEquals("", CharSequenceTranslator.translate(Character.toString(new char[0])));
    }

    @Test
    public void testTranslateSingleCharacter() {
        String input = "A";
        String expected = "A";
        assertEquals(expected, CharSequenceTranslator.translate(input));
    }

    @Test
    public void testTranslateSingleCharacterWithWriter() {
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(Character.toString('A'), out);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateStringWithWriter() {
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("ABC", out);
        assertEquals("ABC", out.toString());
    }

    @Test
    public void testTranslateMultipleCharacters() {
        String input = "ABC";
        String expected = "ABC";
        assertEquals(expected, CharSequenceTranslator.translate(input));
    }

    @Test
    public void testTranslateMultipleCharactersWithWriter() {
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, out);
        assertEquals("ABC", out.toString());
    }

    @Test
    public void testTranslateSurrogatePair() {
        String input = "\uD840\uDC00";
        String expected = "\uD840\uDC00";
        assertEquals(expected, CharSequenceTranslator.translate(input));
    }

    @Test
    public void testTranslateSurrogatePairWithWriter() {
        Writer out = new StringWriter();
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, out);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = identity.with(hexTranslator);
        String input = "ABC";
        String expected = "414243";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithAggregateTranslatorWithWriter() {
        Writer out = new StringWriter();
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = identity.with(hexTranslator);
        translator.translate(input, out);
        assertEquals(expected, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, null);
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex((int) c);
            out.write(hex);
            return hex.length();
        }
    }

    private static class AggregateTranslator implements CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                index += consumed;
                if (Character.isSurrogate(Character.codePointAt(input, index))) {
                    index++;
                }
                if (index >= input.length()) {
                    break;
                }
            }
            return consumed;
        }

        @Override
        public String translate(CharSequence input) {
            StringWriter out = new StringWriter();
            translate(input, out);
            return out.toString();
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            int index = 0;
            for (CharSequenceTranslator translator : translators) {
                int consumed = translator.translate(input, index, out);
                index += consumed;
                if (Character.isSurrogate(Character.codePointAt(input, index))) {
                    index++;
                }
                if (index >= input.length()) {
                    break;
                }
            }
        }
    }
}