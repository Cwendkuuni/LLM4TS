package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " foo bar baz ";
        String expected = " foo bar baz ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " foo bar baz ";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = " foo bar baz ";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriterIOException() throws IOException {
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Writer error");
            }
        };
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " foo bar baz ";
        try {
            translator.translate(input, out);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = " foo bar baz ";
        String expected = " FOO BAR BAZ ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateTranslatorWithNullInput() {
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);
        String input = null;
        String expected = null;
        String result = translator.translate(input);
        assertNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateTranslatorWithNullTranslator() {
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator[] translators = new CharSequenceTranslator[] { translator1 };
        try {
            translator1 = null;
            CharSequenceTranslator translator = new AggregateTranslator(translators);
            String input = " foo bar baz ";
            String expected = " foo bar baz ";
            String result = translator.translate(input);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testTranslateWithAggregateTranslatorWithEmptyArray() {
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[0]);
        String input = " foo bar baz ";
        String expected = " foo bar baz ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " foo\u00E1re bar\uD840\uDC05 baz ";
        String expected = " foo\u00E1re bar\uD840\uDC05 baz ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithUpperCaseTranslator() {
        CharSequenceTranslator translator = new UpperCaseTranslator();
        String input = " foo\u00E1re bar\uD840\uDC05 baz ";
        String expected = " FOO\u00E1RE BAR\uD840\uDC05 BAZ ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= Character.codePointCount(input, 0, input.length())) {
                return 0;
            }
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            return Character.charCount(c[0]);
        }
    }

    private static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char codePoint = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex(codePoint);
            out.write(hex.charAt(0));
            out.write(hex.charAt(1));
            return 2;
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int length = 0;
            for (CharSequenceTranslator translator : translators) {
                int translated = translator.translate(input, index, out);
                length += translated;
                if (length >= Character.codePointCount(input, index, input.length())) {
                    break;
                }
                index += translated;
            }
            return length;
        }
    }
}