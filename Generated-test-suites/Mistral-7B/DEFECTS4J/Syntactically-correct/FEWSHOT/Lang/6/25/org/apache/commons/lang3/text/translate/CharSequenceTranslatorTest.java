package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
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
        assertEquals("", translator.translate(Character.MIN_VALUE, Character.MIN_VALUE, new StringWriter()));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("a", translator.translate("abcd".subSequence(0, 1), 0, writer).toString());
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("abcd", translator.translate("abcd".subSequence(0, 4), 0, writer).toString());
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("abcd", translator.translate("abcd".subSequence(0, 4), 0, writer));
        assertEquals("abcd", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("abcd".subSequence(0, 4), 0, null);
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(uppercase);
        StringWriter writer = new StringWriter();
        assertEquals("Abcd", translator.translate("abcd".subSequence(0, 4), 0, writer).toString());
    }

    @Test
    public void testTranslateWithIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("foo bar baz", translator.translate("foo bar baz".split(" "), 0, writer).toString());
    }

    @Test
    public void testTranslateWithUpperCaseTranslator() {
        CharSequenceTranslator translator = new UpperCaseTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("FOO BAR BAZ", translator.translate("foo bar baz".split(" "), 0, writer).toString());
    }

    @Test
    public void testTranslateWithHexTranslator() {
        CharSequenceTranslator translator = new HexTranslator();
        StringWriter writer = new StringWriter();
        assertEquals("41424344", translator.translate("abcd".subSequence(0, 4), 0, writer).toString());
    }

    @Test
    public void testTranslateWithCombinedTranslators() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UpperCaseTranslator();
        CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator translator = identity.with(uppercase).with(hex);
        StringWriter writer = new StringWriter();
        assertEquals("41424344", translator.translate("abcd".subSequence(0, 4), 0, writer).toString());
    }

    private static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return c.length;
        }
    }

    private static class UpperCaseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.toUpperCase(Character.codePointAt(input, index));
            out.write(Character.name(c));
            return Character.charCount(c);
        }

    }

    private static class HexTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int codepoint = Character.codePointAt(input, index);
            out.write(hex(codepoint).toCharArray()[0]);
            out.write(hex(codepoint).toCharArray()[1]);
            return 2;
        }

    }

    private static class AggregateTranslator extends CharSequenceTranslator {

        private CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int totalConsumed = 0;
            for (CharSequenceTranslator translator : translators) {
                int consumed = translator.translate(input, index, out);
                if (consumed > 0) {
                    totalConsumed += consumed;
                    index += consumed;
                } else {
                    index++;
                }
            }
            return totalConsumed;
        }

    }

}