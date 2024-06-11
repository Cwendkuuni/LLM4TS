package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateNull() {
        CharSequenceTranslator translator = new SimpleTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmpty() {
        CharSequenceTranslator translator = new SimpleTranslator();
        assertEquals("", translator.translate(CharSequences.EMPTY));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        assertEquals("A", translator.translate("A"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new SimpleTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequences.EMPTY, writer);
        assertEquals("", writer.toString());

        writer = new StringWriter();
        translator.translate("A", writer);
        assertEquals("A", writer.toString());

        writer = new StringWriter();
        translator.translate("ABC", writer);
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateWithAggregate() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UppercaseTranslator();
        CharSequenceTranslator translator = new SimpleTranslator().with(identity, uppercase);

        assertEquals("abc", translator.translate("abc"));
        assertEquals("ABC", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithCustomTranslator() {
        CharSequenceTranslator translator = new SimpleTranslator().with(new CustomTranslator());
        assertEquals("X", translator.translate("A"));
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        translator.translate("ABC", null);
    }

    private static class CustomTranslator extends SimpleTranslator {
        @Override
        protected char translateChar(int codepoint) {
            return (char) (codepoint + 10);
        }
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            out.write(Character.toChars(Character.codePointAt(input, index))[0]);
            return Character.charCount(Character.codePointAt(input, index));
        }
    }

    private static class SimpleTranslator extends CharSequenceTranslator {
        @Override
        protected char translateChar(int codepoint) {
            return (char) codepoint;
        }
    }

    private static class UppercaseTranslator extends CharSequenceTranslator {
        @Override
        protected char translateChar(int codepoint) {
            if (Character.isLetter(codepoint)) {
                return (char) (codepoint - 'a' + 'A');
            } else {
                return (char) codepoint;
            }
        }
    }

    static CharSequence CharSequences = new CharSequence() {
        private final char[] chars;

        CharSequence(char[] chars) {
            this.chars = chars;
        }

        @Override
        public int length() {
            return chars.length;
        }

        @Override
        public char charAt(int index) {
            return chars[index];
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new String(chars, start, end - start);
        }
    };
}