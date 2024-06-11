package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

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
    public void testTranslateIdentity() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("foo", translator.translate("foo"));
    }

    @Test
    public void testTranslateWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("foo".toCharArray(), 0, writer);
        assertEquals("foo", writer.toString());
    }

    @Test
    public void testTranslateAggregate() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator translator = identity.with(hex);
        StringWriter writer = new StringWriter();
        translator.translate("foo\u00E1\uD840\uDC35".toCharArray(), 0, writer);
        assertEquals("foo%C3%A1%ED%84%A0%DC%35", writer.toString());
    }

    @Test
    public void testTranslateIdentityWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("foo", writer);
        assertEquals("foo", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("foo", null);
    }

    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c)[0]);
            return Character.charCount(c);
        }
    }

    static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c)) {
                out.write(Character.toChars(c)[0]);
                out.write(Character.toChars(Character.forDigit(Character.digit(c), 16))[0]);
                return 1;
            } else if (Character.isLetter(c) && Character.isUpperCase(c)) {
                out.write(Character.toChars(Character.toUpperCase(c) - 65 + 10)[0]);
                out.write(Character.toChars(Character.toUpperCase(c) - 65 + 10)[0]);
                return 1;
            } else if (Character.isLetter(c) && Character.isLowerCase(c)) {
                out.write(Character.toChars(Character.toUpperCase(c) - 97 + 10)[0]);
                out.write(Character.toChars(Character.toUpperCase(c) - 97 + 10)[0]);
                return 1;
            }
            out.write(Character.toChars(c)[0]);
            return Character.charCount(c);
        }
    }

    static class AggregateTranslator extends CharSequenceTranslator {
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

}