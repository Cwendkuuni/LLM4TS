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
        assertEquals("", translator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate(CharSequence.class.cast("A")));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("AB", translator.translate(CharSequence.class.cast("AB")));
    }

    @Test
    public void testTranslateWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer writer = new StringWriter();
        translator.translate(CharSequence.class.cast("ABCD"), writer);
        assertEquals("ABCD", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator upperCaseTranslator = new UpperCaseTranslator();

        CharSequenceTranslator translator = identityTranslator.with(upperCaseTranslator);

        Writer writer = new StringWriter();
        translator.translate(CharSequence.class.cast("abcd"), writer);
        assertEquals("ABCD", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslatorOrder() throws IOException {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator upperCaseTranslator = new UpperCaseTranslator();
        CharSequenceTranslator reverseTranslator = new ReverseTranslator();

        CharSequenceTranslator translator = reverseTranslator.with(identityTranslator, upperCaseTranslator);

        Writer writer = new StringWriter();
        translator.translate(CharSequence.class.cast("ABCD"), writer);
        assertEquals("dCBA", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(CharSequence.class.cast("ABCD"), null);
    }

    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("foo", translator.translate(CharSequence.class.cast("foo")));
    }

    @Test
    public void testUpperCaseTranslator() {
        CharSequenceTranslator translator = new UpperCaseTranslator();
        assertEquals("FOO", translator.translate(CharSequence.class.cast("foo")));
    }

    @Test
    public void testLowerCaseTranslator() {
        CharSequenceTranslator translator = new LowerCaseTranslator();
        assertEquals("foo", translator.translate(CharSequence.class.cast("FOO")));
    }

    @Test
    public void testReverseTranslator() {
        CharSequenceTranslator translator = new ReverseTranslator();
        assertEquals("d", translator.translate(CharSequence.class.cast("A")));
    }

    @Test
    public void testTranslateWithCodePoint() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("\u0041", translator.translate(CharSequence.class.cast("\u0041"), 0, 1));
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\uD800\uDC00";
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return new String(input);
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            String s = translate(input);
            out.write(s.toCharArray(), 0, s.length());
        }
    }

    static class UpperCaseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(Character.toUpperCase(c)));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return Character.toUpperCase(Character.codePointAt(input, 0)) + translate(input.subSequence(1, input.length()));
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            String s = translate(input);
            out.write(s.toCharArray(), 0, s.length());
        }
    }

    static class LowerCaseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(Character.toLowerCase(c)));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return Character.toLowerCase(Character.codePointAt(input, 0)) + translate(input.subSequence(1, input.length()));
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            String s = translate(input);
            out.write(s.toCharArray(), 0, s.length());
        }
    }

    static class ReverseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(Character.toUpperCase(c)));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder sb = new StringBuilder();
            int len = input.length();
            for (int i = len - 1; i >= 0; i--) {
                char c = Character.codePointAt(input, i);
                sb.append(Character.toString(Character.toChars(Character.toLowerCase(c))).charAt(0));
            }
            return sb.reverse().toString();
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            String s = translate(input);
            out.write(s.toCharArray(), 0, s.length());
        }
    }

}