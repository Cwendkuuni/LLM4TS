package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        String expected = "abcdefghijklmnopqrstuvwxyz";

        String result = translator.translate(input);
        assertEquals(expected, result);

        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream(100));
        translator.translate(input, writer);
        String resultFromWriter = writer.toString();
        assertEquals(expected, resultFromWriter);

        // Test with null input
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        // Identity translator
        CharSequenceTranslator identity = new IdentityTranslator();

        // Uppercase translator
        CharSequenceTranslator uppercase = new UpperCaseTranslator();

        // Combine translators
        CharSequenceTranslator translator = identity.with(uppercase);

        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        String result = translator.translate(input);
        assertEquals(expected, result);

        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream(100));
        translator.translate(input, writer);
        String resultFromWriter = writer.toString();
        assertEquals(expected, resultFromWriter);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";

        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream(100));
        translator.translate(input, writer);
        String resultFromWriter = writer.toString();
        String expected = "abcdefghijklmnopqrstuvwxyz";
        assertEquals(expected, resultFromWriter);

        // Test with surrogate pair
        String inputWithSurrogate = "\uD800\uDC00";
        CharSequence cs = Character.isSurrogate((char) 0xD800) ? Character.toString(Character.toCodePoint((char) 0xD800, (char) 0xDC00)) : inputWithSurrogate;
        translator.translate(cs, writer);
        String resultFromWriterWithSurrogate = writer.toString();
        assertEquals(Character.codePointAt(cs, 0) + Character.codePointAt(cs, 1), Integer.parseInt(resultFromWriterWithSurrogate, 16));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        translator.translate(input, null);
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }
    }

    private static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isUpperCase(c)) {
                out.write(Character.toChars(Character.toLowerCase(c, Locale.ENGLISH)));
            } else {
                out.write(Character.toChars(Character.toUpperCase(c, Locale.ENGLISH)));
            }
            return Character.charCount(c);
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
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
}