package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateToString() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String output = translator.translateToString(input);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new StringWriter();
        translator.translate(null, 0, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a null writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a empty input
     */
    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new StringWriter();
        translator.translate("", 0, out);
        String output = out.toString();
        assertEquals("", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     */
    @Test
    public void testTranslateWithUnicode() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\u00E9llo, \u00E0ld\u00F3!";
        StringWriter out = new StringWriter();
        translator.translate(input, 0, out);
        String output = out.toString();
        String expected = "\u00E9llo, \u00E0ld\u00F3!";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateToStringWithUnicode() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\u00E9llo, \u00E0ld\u00F3!";
        String output = translator.translateToString(input);
        String expected = "\u00E9llo, \u00E0ld\u00F3!";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple translators
     */
    @Test
    public void testTranslateWithMultipleTranslators() throws Exception {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String input = "Hello, World!";
        StringWriter out = new StringWriter();
        combinedTranslator.translate(input, 0, out);
        String output = out.toString();
        String expected = "HELLO, WORLD!";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translateToStringWithMultipleTranslators()}
     */
    @Test
    public void testTranslateToStringWithMultipleTranslators() throws Exception {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String input = "Hello, World!";
        String output = combinedTranslator.translateToString(input);
        String expected = "HELLO, WORLD!";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translateToStringWithEmptyInput()}
     */
    @Test
    public void testTranslateToStringWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translateToString("");
        assertNull(output);
    }

    /**
     * Helper class for aggregating translators
     */
    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int offset = 0;
            for (CharSequenceTranslator translator : translators) {
                int consumed = translator.translate(input, index + offset, out);
                if (consumed == 0) {
                    char c = Character.codePointAt(input, index + offset);
                    out.write(c);
                    offset++;
                    continue;
                }
                offset += consumed;
            }
            return offset;
        }

        @Override
        public String translateToString(CharSequence input) throws IOException {
            StringWriter out = new StringWriter();
            translate(input, out);
            return out.toString();
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            int length = input.length();
            int offset = 0;
            for (CharSequenceTranslator translator : translators) {
                int remain = length - offset;
                int consumed = translator.translate(input, offset, out);
                if (consumed == 0) {
                    char c = Character.codePointAt(input, offset);
                    out.write(c);
                    offset++;
                } else {
                    offset += consumed;
                }
                if (offset >= length) {
                    break;
                }
            }
        }
    }

    /**
     * Identity translator that doesn't do anything
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translateToString(CharSequence input) throws IOException {
            return StringUtils.toString(input);
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            int length = input.length();
            for (int i = 0; i < length; i++) {
                char c = Character.codePointAt(input, i);
                out.write(c);
            }
        }
    }

    /**
     * Translator that translates all characters to upper case
     */
    private static class ToUpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toUpperCase(c));
            return Character.charCount(Character.codePointAt(input, index));
        }

        @Override
        public String translateToString(CharSequence input) throws IOException {
            return StringUtils.upperCase(StringUtils.toString(input));
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            int length = input.length();
            for (int i = 0; i < length; i++) {
                char c = Character.codePointAt(input, i);
                out.write(Character.toUpperCase(c));
            }
        }
    }
}