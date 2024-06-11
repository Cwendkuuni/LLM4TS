package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method of CharSequenceTranslator.
     */
    @Test
    public void testTranslate() {
        String input = "Hello, 世界!";
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(input);
        assertEquals(input, output);

        translator = new AggregateTranslator(new IdentityTranslator(), new HexTranslator());
        output = translator.translate(input);
        String expected = "5468656C6C6F2C E4B8A6AC E79982B9 21";
        assertEquals(expected, output);

        try {
            CharSequenceTranslator invalidTranslator = new CharSequenceTranslator() {
                @Override
                public int translate(CharSequence input, int index, Writer out) throws IOException {
                    throw new UnsupportedOperationException();
                }
            };
            output = invalidTranslator.translate(input);
            fail("Expected IllegalStateException when using an invalid translator.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    /**
     * Test for translate method of Writer usage.
     */
    @Test
    public void testTranslateWriter() {
        String input = "Hello, 世界!";
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals(input, output);

        translator = new AggregateTranslator(new IdentityTranslator(), new HexTranslator());
        writer = new StringWriter();
        translator.translate(input, writer);
        output = writer.toString();
        String expected = "5468656C6C6F2C E4B8A6AC E79982B9 21";
        assertEquals(expected, output);
    }

    /**
     * Test for with method of CharSequenceTranslator.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new HexTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);

        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        String expected = "5468656C6C6F2C E4B8A6AC E79982B9 21";
        assertEquals(expected, output);
    }

    /**
     * Identity translator that does not change the input.
     */
    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char[] chars = Character.toChars(Character.codePointAt(input, index));
            int len = chars.length;
            out.write(chars, 0, len);
            return len;
        }
    }

    /**
     * Hex translator that converts characters to uppercase hexadecimal string.
     */
    static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex((int) c);
            out.write(hex);
            return hex.length();
        }
    }
}