package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CharSequenceTranslator} and {@link AggregateTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslateMethod() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";

        StringWriter output = new StringWriter();
        int consumed = translator.translate(CharSequenceUtils.subSequence(input, 0, 6), 0, output);
        assertEquals("Hello", output.toString());
        assertEquals(6, consumed);

        output.getBuffer().setLength(0); // clear the buffer for next test

        consumed = translator.translate(CharSequenceUtils.subSequence(input, 6, input.length()), 0, output);
        assertEquals("世界!", output.toString());
        assertEquals(6, consumed);

        // test with invalid arguments
        try {
            translator.translate(null, 0, output);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            translator.translate(CharSequenceUtils.subSequence(input, 0, 6), -1, output);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            translator.translate(CharSequenceUtils.subSequence(input, 0, 6), 7, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            translator.translate(CharSequenceUtils.subSequence(input, 0, 6), 0, output);
            output.write("X"); // write something to the writer
            translator.translate(CharSequenceUtils.subSequence(input, 6, input.length()), 0, output);
            String result = output.toString();
            assertEquals("Hello世界!X", result);
        } finally {
            output.close();
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateMethodWithStringWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";

        String result = translator.translate(CharSequenceUtils.subSequence(input, 0, 6));
        assertEquals("Hello", result);

        result = translator.translate(CharSequenceUtils.subSequence(input, 6, input.length()));
        assertEquals("世界!", result);

        // test with invalid arguments
        try {
            translator.translate(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        try {
            translator.translate(CharSequenceUtils.subSequence(input, 0, 6).subSequence(6, 6));
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            translator.translate(CharSequenceUtils.subSequence(input, 0, 6).subSequence(0, -1));
            fail("Expected NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            // expected
        }

        // test multiple translations in one call
        try {
            String result = translator.translate(input);
            assertEquals("Hello世界!", result);
        } finally {
            // clear the writer's buffer
            StringUtils.empty(System.getProperty("java.io.tmpdir") + "/junit-output.txt");
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testComposeTranslators() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();

        CharSequenceTranslator compositeTranslator = identityTranslator.with(uppercaseTranslator);

        String input = "Hello, 世界!";
        StringWriter output = new StringWriter();

        compositeTranslator.translate(CharSequenceUtils.subSequence(input, 0, 6), 0, output);
        assertEquals("Hello", output.toString());

        output.getBuffer().setLength(0); // clear the buffer for next test

        compositeTranslator.translate(CharSequenceUtils.subSequence(input, 6, input.length()), 0, output);
        assertEquals("世界!", output.toString());
        assertEquals("ウorld!", output.toString()); // uppercase

        output.close();
    }

    /**
     * Identity translator that does nothing.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = Character.codePointCount(input, index, index + 1);
            if (len == 0) {
                return 0;
            }
            out.write(Character.toChars(Character.codePointAt(input, index)));
            return len;
        }
    }

    /**
     * Translator that converts all characters to uppercase.
     */
    private static class UppercaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = Character.codePointCount(input, index, index + 1);
            if (len == 0) {
                return 0;
            }
            out.write(Character.toChars(Character.codePointAt(input, index).toUpperCase(Locale.ENGLISH)));
            return len;
        }
    }

    /**
     * Translator that combines multiple translators.
     */
    private static class AggregateTranslator implements CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                if (consumed == 0) {
                    index += Character.charCount(Character.codePointAt(input, index));
                }
            }
            return consumed;
        }
    }
}