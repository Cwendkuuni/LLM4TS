package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("22", CharSequenceTranslator.hex(0x22));
        assertEquals("00", CharSequenceTranslator.hex(0x0));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast("")));
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
        Writer writer = new StringWriter();
        assertEquals("", translator.translate(CharSequence.class.cast(""), 0, writer));
        assertEquals("ABC", translator.translate("ABC", 0, writer));
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregatedTranslator = identityTranslator.with(hexTranslator);
        Writer writer = new StringWriter();
        assertEquals("A041C", aggregatedTranslator.translate("AC", 0, writer));
        assertEquals("A041C", writer.toString());
    }

    static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }

    static class HexTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c)) {
                out.write(c);
                out.write(CharSequenceTranslator.hex(Character.digit(c, 16)));
                return 2;
            } else if (Character.isLetter(c) && Character.isUpperCase(c)) {
                out.write(CharSequenceTranslator.hex(Character.toUpperCase(c) - 'A' + 10));
                return 1;
            } else {
                return 0;
            }
        }
    }

    static class AggregateTranslator extends CharSequenceTranslator {

        private final CharSequenceTranslator[] translators;

        AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                index += consumed;
                if (consumed == input.length()) {
                    break;
                }
            }
            return consumed;
        }
    }
}