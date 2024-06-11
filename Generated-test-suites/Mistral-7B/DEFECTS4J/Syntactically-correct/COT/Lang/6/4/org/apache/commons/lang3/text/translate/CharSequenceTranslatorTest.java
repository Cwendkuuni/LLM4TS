package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateAbstractMethod() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("test@example.com");
        StringWriter out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals("test%40example.com", out.toString());
        assertEquals(input.length(), consumed);
    }

    @Test
    public void testTranslateFinalMethod() {
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "test@example.com";
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        assertEquals("test%40example.com", out.toString());
    }

    @Test
    public void testTranslateWith() {
        CharSequenceTranslator noopTranslator = new NoopTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = noopTranslator.with(hexTranslator);
        CharSequence input = "test@example.com";
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        String expected = "test%40example.com";
        assertEquals(expected, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("test@example.com");
        translator.translate(input, null);
    }

    @Test
    public void testTranslateQuotedPrintable() throws IOException {
        CharSequenceTranslator translator = new QuotedPrintableTranslator();
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("test@example.com");
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        String expected = "test%40example.com";
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslateEmpty() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "";
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "a";
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        assertEquals("a", out.toString());
    }

    @Test
    public void testTranslateMultiCharacter() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "abcd";
        StringWriter out = new StringWriter();
        translator.translate(input, out);
        assertEquals("abcd", out.toString());
    }

    @Test
    public void testTranslateMiddleOfSequence() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "abcd";
        StringWriter out = new StringWriter();
        int index = 2;
        int consumed = translator.translate(input, index, out);
        assertEquals("cd", out.toString());
        assertEquals(2, consumed);
    }

    @Test
    public void testTranslateAtBeginning() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "abcd";
        StringWriter out = new StringWriter();
        int index = 0;
        int consumed = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslateAtEnd() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "abcd";
        StringWriter out = new StringWriter();
        int index = 4;
        int consumed = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, consumed);
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "\uD800\uDC00";
        StringWriter out = new StringWriter();
        translator.translate(input, 0, out);
        assertEquals("\uD800\uDC00", out.toString());
    }

    @Test
    public void testTranslateWithSurrogatePairAtBeginning() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "\uD800\uDC00";
        StringWriter out = new StringWriter();
        int index = 0;
        int consumed = translator.translate(input, index, out);
        assertEquals("\uD800\uDC00", out.toString());
        assertEquals(2, consumed);
    }

    @Test
    public void testTranslateWithSurrogatePairInMiddle() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "ab\uD800\uDC00cd";
        StringWriter out = new StringWriter();
        int index = 3;
        int consumed = translator.translate(input, index, out);
        assertEquals("cd", out.toString());
        assertEquals(2, consumed);
    }

    @Test
    public void testTranslateWithSurrogatePairAtEnd() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequence input = "abcd\uD800\uDC00";
        StringWriter out = new StringWriter();
        int index = 7;
        int consumed = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, consumed);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithIOException() throws IOException {
        CharSequenceTranslator translator = new IOExceptionTranslator();
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("test@example.com");
        StringWriter out = new StringWriter();
        translator.translate(input, 0, out);
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.isSurrogate((char) Character.codePointAt(input, index++));
            if (c != Character.HIGH_SURROGATE) {
                out.write(Character.codePointAt(input, index - 1));
            }
            return Character.isSurrogate(c) ? 2 : 1;
        }
    }

    private static class NoopTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index++);
            String hex = CharSequenceTranslator.hex(Character.codePointAt(input, index));
            out.write(hex);
            return 2;
        }
    }

    private static class QuotedPrintableTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            while (index < input.length() && Character.isWhitespace((char) Character.codePointAt(input, index))) {
                index++;
            }
            if (index >= input.length()) {
                return consumed;
            }
            if (Character.codePointAt(input, index) == '=') {
                index++;
                if (Character.isWhitespace((char) Character.codePointAt(input, index))) {
                    index++;
                }
                if (index >= input.length()) {
                    return consumed;
                }
                int len = Character.codePointAt(input, index) >> 4;
                index++;
                int b1 = Character.codePointAt(input, index);
                index++;
                int b2 = Character.codePointAt(input, index);
                out.write(len << 4 | (b1 & 0xF));
                out.write(b2 & 0xF);
                consumed += 3;
            } else {
                out.write(Character.codePointAt(input, index++));
                consumed++;
            }
            return consumed;
        }
    }

    private static class IOExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
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