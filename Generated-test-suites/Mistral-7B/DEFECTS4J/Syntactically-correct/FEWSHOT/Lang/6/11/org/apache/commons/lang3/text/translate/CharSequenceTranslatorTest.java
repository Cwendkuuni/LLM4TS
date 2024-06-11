package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String expected = input;
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        String result = writer.toString();
        assertEquals("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Writer writer = null;
        translator.translate(input, writer);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ReversedTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String expected = new String(new byte[]{'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'A', 'Z', '.'});
        String result = compositeTranslator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateTranslatorWithWriter() throws Exception {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ReversedTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);
        String input = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Writer writer = new StringWriter();
        compositeTranslator.translate(input, writer);
        String result = writer.toString();
        assertEquals(new String(new byte[]{'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', 'p', 'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'A', 'Z', '.'}), result);
    }

    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            if (input == null || index < 0 || index >= input.length()) {
                return -1;
            }
            char c = Character.codePointAt(input, index);
            StringWriter sw = (StringWriter) out;
            sw.write(Character.toString(c));
            consumed += Character.charCount(c);
            return consumed;
        }
    }

    static class ReversedTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            if (input == null || index < 0 || index >= input.length()) {
                return -1;
            }
            char c = Character.codePointAt(input, index);
            StringWriter sw = (StringWriter) out;
            sw.write(Character.toString((char) (0xFF & (c >>> 5))));
            sw.write(Character.toString((char) (0xFF & (c >>> 3))));
            sw.write(Character.toString((char) (0xFF & (c >>> 1))));
            sw.write(Character.toString((char) (0xFF & (c >>> 0))));
            consumed += 4;
            return consumed;
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
            CharSequenceTranslator translator = this;
            while (consumed < Integer.MAX_VALUE && index < input.length()) {
                int translateResult = translator.translate(input, index, out);
                if (translateResult < 0) {
                    throw new IOException("Translator returned a negative number of consumed codepoints");
                }
                consumed += translateResult;
                index += translateResult;
                translator = translators[translator.hashCode() & (translators.length - 1)];
            }
            return consumed;
        }
    }
}