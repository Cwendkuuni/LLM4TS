package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(Character.toString(new char[0]), 0, new StringWriter()));

        String input = "foo bar baz";
        String expected = "foo bar baz";
        String result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        StringWriter writer = new StringWriter();
        translator.translate(Character.toString("foo bar baz").toCharArray(), 0, writer);
        assertEquals(expected, writer.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString("foo bar baz").toCharArray(), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());

        input = "foo\u0061\u0062";
        expected = "foo a b";
        result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testTranslateWith() throws IOException {
        CharSequenceTranslator translator1 = new HexTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);

        String input = "foo";
        String expected = "foo%66%6f";
        String result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testIdentityTranslator() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo";
        String expected = "foo";
        String result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testHexTranslator() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        String input = "foo";
        String expected = "foo%66%6f";
        String result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());

        input = "f\u0066o";
        expected = "f%66o";
        result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new HexTranslator();
        CharSequenceTranslator translator2 = new UpperCaseTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);

        String input = "foo";
        String expected = "FOO%66%6f";
        String result = translator.translate(Character.toString(input.toCharArray()), 0, new StringWriter());
        assertEquals(expected, result);

        writer = new StringWriter();
        translator.translate(Character.toString(input.toCharArray()), 0, writer);
        assertEquals(expected, writer.toString());

        outputStream = new ByteArrayOutputStream();
        translator.translate(Character.toString(input.toCharArray()), 0, outputStream.getWriter());
        assertEquals(expected.getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testNullInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null, 0, new StringWriter()));
    }

    @Test
    public void testNullWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(Character.toString("foo".toCharArray()), 0, null));
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int c = Character.codePointAt(input, index);
            if (Character.isSurrogate(c)) {
                char c1 = Character.highSurrogate(c);
                char c2 = Character.lowSurrogate(c);
                out.write(c1);
                out.write(c2);
                return Character.charCount(c);
            }
            out.write(c);
            return 1;
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

    private static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int codepoint = Character.codePointAt(input, index);
            out.write(Character.toUpperCase((char) codepoint));
            return 1;
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator... translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                index += consumed;
                if (index >= input.length()) {
                    break;
                }
            }
            return consumed;
        }
    }
}