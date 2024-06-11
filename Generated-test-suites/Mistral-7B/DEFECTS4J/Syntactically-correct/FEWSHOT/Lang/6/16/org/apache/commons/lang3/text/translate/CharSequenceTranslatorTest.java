package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

/**
 * Test cases for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        String expected = "foo bar baz";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String result = writer.toString();
        assertEquals("foo bar baz", result);
    }

    @Test
    public void testTranslateToWriterWithIOException() throws IOException {
        CharSequenceTranslator translator = new ThrowingTranslator();
        CharSequence input = "foo bar baz";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testAggregateTranslator() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator aggregator = identity.with(hex);

        CharSequence input = "foo bar baz";
        StringWriter writer = new StringWriter();
        aggregator.translate(input, writer);
        String result = writer.toString();
        String expected = "foo" + System.lineSeparator() + "66" + System.lineSeparator()
                + "6F" + System.lineSeparator() + "6F" + System.lineSeparator() + "62" + System.lineSeparator()
                + "61" + System.lineSeparator() + "6B" + System.lineSeparator() + "61" + System.lineSeparator()
                + "6B" + System.lineSeparator() + "61" + System.lineSeparator() + "6B";
        assertEquals(expected, result);
    }

    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo";
        String result = translator.translate(input);
        assertEquals("foo", result);
    }

    @Test
    public void testHexTranslator() {
        CharSequenceTranslator translator = new HexTranslator();
        CharSequence input = "foo";
        String result = translator.translate(input);
        assertNull(result);

        input = "\u0066";
        result = translator.translate(input);
        assertEquals("42", result);

        input = "\u0066\u006F";
        result = translator.translate(input);
        assertEquals("4246", result);
    }

    @Test
    public void testNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = null;
        String result = translator.translate(input);
        assertNull(result);
    }

    @Test
    public void testNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo";
        Writer out = null;
        try {
            translator.translate(input, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }
    }

    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex(c);
            out.write(hex.toCharArray());
            return hex.length();
        }
    }

    private static class ThrowingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Test IOException");
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int result = 0;
            for (CharSequenceTranslator translator : translators) {
                result += translator.translate(input, index, out);
                index += result;
            }
            return result;
        }

        @Override
        public String translate(CharSequence input) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(byteStream);
            try {
                translate(input, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                writer.close();
            }
            return new String(byteStream.toByteArray());
        }
    }
}