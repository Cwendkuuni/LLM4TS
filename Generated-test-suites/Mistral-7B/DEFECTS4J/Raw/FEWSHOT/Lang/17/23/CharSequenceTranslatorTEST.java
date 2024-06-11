package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateStringWriter() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter(1024);
        CharSequence input = "foo bar baz";
        translator.translate(input, writer);
        assertEquals("foo bar baz", writer.toString());
    }

    @Test
    public void testTranslateNullInput() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWriterNull() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(new String("foo"), null));
    }

    @Test
    public void testTranslateWithEmptyInput() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(CharSequence.class.cast(new String(""))));
    }

    @Test
    public void testTranslateSingleCharacter() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("a", translator.translate(CharSequence.class.cast(new String("a"))));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo bar baz";
        String expected = "foo bar baz";
        String result = translator.translate(CharSequence.class.cast(input)).toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\uD800\uDC00";
        String expected = "\uD800\uDC00";
        String result = translator.translate(CharSequence.class.cast(input)).toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithMultipleCodePoints() {
        final CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo\u0303bar";
        String expected = "fooḡbar";
        String result = translator.translate(CharSequence.class.cast(input)).toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        final CharSequenceTranslator identity = new IdentityTranslator();
        final CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        StringWriter writer = new StringWriter(1024);
        String input = "foo bar baz";
        composite.translate(input, writer);
        String result = writer.toString();
        String expected = "666F6F 626161 62617A";
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithAggregateTranslatorAndNullInput() {
        final CharSequenceTranslator identity = new IdentityTranslator();
        final CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        assertNull(composite.translate(null));
    }

    @Test
    public void testTranslateWithAggregateTranslatorAndEmptyInput() {
        final CharSequenceTranslator identity = new IdentityTranslator();
        final CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        assertEquals("", composite.translate(CharSequence.class.cast(new String(""))));
    }

    @Test
    public void testTranslateWithAggregateTranslatorAndSingleCharacter() {
        final CharSequenceTranslator identity = new IdentityTranslator();
        final CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        assertEquals("61", composite.translate(CharSequence.class.cast(new String("a"))));
    }

    @Test
    public void testTranslateWithAggregateTranslatorAndMultipleCharacters() {
        final CharSequenceTranslator identity = new IdentityTranslator();
        final CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator composite = identity.with(hex);
        String input = "foo bar baz";
        String expected = "666F6F 626161 62617A";
        String result = composite.translate(CharSequence.class.cast(input)).toString();
        assertEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithStringWriterIOException() {
        final CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("a");
                throw new IOException();
            }
        };
        StringWriter writer = new StringWriter(1024);
        CharSequence input = "foo bar baz";
        translator.translate(input, writer);
    }

    private static final class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                char c = Character.codePointAt(input, index);
                if (Character.isSurrogate(c)) {
                    char highSurrogate = Character.highSurrogate(c);
                    char lowSurrogate = Character.lowSurrogate(c);
                    out.write(highSurrogate);
                    out.write(lowSurrogate);
                    return 2;
                }
                out.write(c);
                return 1;
            }
            return 0;
        }
    }

    private static final class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            int codepoint = Character.codePointForName(Character.NAMES[Character.toUpperCase(c)]);
            if (codepoint < 0) {
                return 0;
            }
            String hex = CharSequenceTranslator.hex(codepoint);
            out.write(hex.charAt(0));
            out.write(hex.charAt(1));
            return 2;
        }
    }

    private static final class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int totalConsumed = 0;
            for (CharSequenceTranslator translator : translators) {
                int consumed = translator.translate(input, index, out);
                totalConsumed += consumed;
                if (consumed == 0) {
                    break;
                }
                index += consumed;
            }
            return totalConsumed;
        }
    }
}