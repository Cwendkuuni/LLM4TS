package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator(); // Identity translator does not translate anything
        CharSequence input = "Hello World!";

        String expected = "Hello World!";
        String result = translator.translate(input);
        assertEquals(expected, result);

        // Test with AggregateTranslator
        CharSequenceTranslator aggregateTranslator = new IdentityTranslator().with(new HexTranslator());
        expected = "48656C6C6F20576F726C6421";
        result = aggregateTranslator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test for translate method with exception.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateWithException() {
        CharSequenceTranslator translator = new ExceptionTranslator();
        CharSequence input = "Hello World!";
        translator.translate(input, new StringWriter(input.length() * 2));
    }

    /**
     * Identity translator does not translate anything.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            out.write(input.charAt(index));
            return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    /**
     * Hex translator converts characters to their uppercase hexadecimal representation.
     */
    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = Integer.toHexString(Character.codePointAt(input, index)).toUpperCase(Locale.ENGLISH);
            out.write(hex.charAt(0));
            out.write(hex.charAt(1));
            return 2;
        }
    }

    /**
     * Exception translator throws an exception when called.
     */
    private static class ExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }
}