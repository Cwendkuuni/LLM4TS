package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello, 世界!";

        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToString() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello, 世界!";

        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";

        StringWriter writer = new StringWriter();
        translator.translate(input, writer);

        String result = writer.toString();
        assertEquals("Hello, 世界!", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";

        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithNullInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = null;

        String result = translator.translate(input);
        assertNull(result);
    }

    @Test
    public void testAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new HexTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();

        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        String input = "Hello, 世界!";
        String expected = "48656c6c6f2C e697a7a521";

        String result = compositeTranslator.translate(input).translateToString();
        assertEquals(expected, result);
    }

    static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }

    static class HexTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex((int) c);
            out.write(hex);
            out.write(" ");
            return hex.length() + 1;
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
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                if (consumed == input.length()) {
                    break;
                }
                index += consumed;
            }
            return consumed;
        }
    }

}