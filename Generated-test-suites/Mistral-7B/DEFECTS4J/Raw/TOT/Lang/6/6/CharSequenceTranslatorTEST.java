package org.apache.commons.lang3.test.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the abstract translate method.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);

        // Test with empty input
        CharSequence emptyInput = "";
        String emptyExpected = "";
        String emptyActual = translator.translate(emptyInput);
        assertEquals(emptyExpected, emptyActual);

        // Test with error scenario, null input
        CharSequence nullInput = null;
        String nullExpected = null;
        assertNull(translator.translate(nullInput));
    }

    /**
     * Test for the translate method using StringWriter.
     */
    @Test
    public void testTranslateToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);

        // Test with empty input
        CharSequence emptyInput = "";
        String emptyExpected = "";
        String emptyActual = translator.translate(emptyInput).toString();
        assertEquals(emptyExpected, emptyActual);

        // Test with error scenario, null input
        CharSequence nullInput = null;
        String nullExpected = null;
        assertNull(translator.translate(nullInput).toString());
    }

    /**
     * Test for the with method.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator mergedTranslator = identityTranslator.with(hexTranslator);

        CharSequence input = "Hello World!";
        String expected = "Hello World!";
        String actual = mergedTranslator.translate(input).toString();
        assertEquals(expected, actual);

        String hexExpected = "48656c6c6f20576f726c6421";
        String hexActual = mergedTranslator.translate(input).toString();
        assertEquals(hexExpected, hexActual);
    }

    /**
     * Identity translator, does not change the input.
     */
    public static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return super.translate(input, index, out);
        }

        @Override
        public String translate(CharSequence input) {
            return super.translate(input).toString();
        }
    }

    /**
     * Hex translator, converts characters to their upper case hexadecimal representation.
     */
    public static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int codePoint = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex(codePoint);
            out.write(hex);
            return hex.length();
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                output.append(CharSequenceTranslator.hex(Character.codePointAt(input, i)));
            }
            return output.toString();
        }
    }

    /**
     * Aggregate translator, merges multiple translators.
     */
    public static class AggregateTranslator extends CharSequenceTranslator {
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
                    char codePoint = Character.codePointAt(input, index);
                    out.write(Character.toChars(codePoint));
                    index++;
                }
                if (consumed > 0) {
                    break;
                }
            }
            return totalConsumed;
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder output = new StringBuilder();
            for (CharSequenceTranslator translator : translators) {
                String translated = translator.translate(input);
                output.append(translated);
            }
            return output.toString();
        }
    }
}