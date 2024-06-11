package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("foo", translator.translate("foo"));
        String result = translator.translate((CharSequence) ("foo bar baz"));
        assertEquals("foo bar baz", result);
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();

        CharSequenceTranslator aggregateTranslator = identityTranslator.with(hexTranslator);

        String input = "foo123bar456baz";
        String expected = "foofoo110101barbar4101010101bazbaz01101101";
        String result = aggregateTranslator.translate((CharSequence) input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        String input = "foo bar baz";

        translator.translate(input, writer);
        String expected = "foo bar baz";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        try {
            translator.translate((CharSequence) ("foo"), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("foo", translator.translate("foo"));
        assertEquals('\u0001', translator.translate("\u0001"));
        assertEquals('\uD800', translator.translate("\uD800"));
        assertEquals('\uDBFF', translator.translate("\uDBFF"));
        assertEquals('\uD800\uDC00', translator.translate("\uD800\uDC00"));
    }

    @Test
    public void testHexTranslator() {
        CharSequenceTranslator translator = new HexTranslator();
        assertEquals("foo", translator.translate("foo"));
        String result = translator.translate((CharSequence) ("foo bar baz"));
        assertEquals("foo00660061006200610072006100630061", result);
        assertSame(translator, translator.with(translator));
    }

    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex(Character.toIntBase(Character.codePointAt(input, index), 16));
            for (int i = 0; i < hex.length(); i++) {
                out.write(hex.charAt(i));
            }
            return 2;
        }
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            CharSequenceTranslator translator = translators[0];
            int consumed = translator.translate(input, index, out);
            for (int i = 1; i < translators.length; i++) {
                translator = translators[i];
                consumed += translator.translate(input, index + consumed, out);
                index += consumed;
            }
            return consumed;
        }
    }
}