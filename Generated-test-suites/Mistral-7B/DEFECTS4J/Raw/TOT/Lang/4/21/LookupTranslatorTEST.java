package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    /**
     * Test the translation with simple lookup table.
     */
    @Test
    public void testSimpleTranslation() {
        final CharSequence[][] lookup = {{"hello", "world"}, {"apache", "commons"}};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final StringBuilder expected = new StringBuilder();
        expected.append("world");
        expected.append(System.lineSeparator());
        expected.append("commons");

        final StringBuilder actual = new StringBuilder();
        final int length = "hello apache".length();

        try (final Writer out = actual) {
            translator.translate("hello apache".toCharArray(), 0, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }

        assertEquals("Expected 'world' and 'commons' but was: ", expected.toString(), actual.toString());
    }

    /**
     * Test the translation with empty lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLookupTable() {
        new LookupTranslator(new CharSequence[0][0]);
    }

    /**
     * Test the translation with non-matching input.
     */
    @Test
    public void testNonMatchingInput() {
        final CharSequence[][] lookup = {"hello", "world"};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final StringBuilder actual = new StringBuilder();
        final int length = "apache commons".length();

        try (final Writer out = actual) {
            translator.translate("apache commons".toCharArray(), 0, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }

        assertEquals("Expected 0 characters to be translated", 0, actual.length());
    }

    /**
     * Test the translation with empty input.
     */
    @Test
    public void testEmptyInput() {
        final CharSequence[][] lookup = {"hello", "world"};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final StringBuilder actual = new StringBuilder();
        final int length = 0;

        try (final Writer out = actual) {
            translator.translate(StringUtils.EMPTY, 0, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }

        assertEquals("Expected 0 characters to be translated", 0, actual.length());
    }

    /**
     * Test the translation with null lookup table and null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        new LookupTranslator(null);
        final CharSequence input = null;

        final StringBuilder actual = new StringBuilder();
        final int index = 0;
        final Writer out = null;

        try {
            translator.translate(input.toCharArray(), index, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }
    }

    /**
     * Test the translation with null input and non-null lookup table.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        final CharSequence[][] lookup = {"hello", "world"};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final CharSequence input = null;
        final StringBuilder actual = new StringBuilder();
        final int index = 0;
        final Writer out = new StringWriter();

        translator.translate(input, index, out);
    }

    /**
     * Test the translation with null lookup table and non-null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        final CharSequence input = "hello apache";
        final LookupTranslator translator = new LookupTranslator(null);

        final StringBuilder actual = new StringBuilder();
        final int index = 0;
        final Writer out = new StringWriter();

        try {
            translator.translate(input.toCharArray(), index, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }
    }

    /**
     * Test the translation with empty lookup table and non-empty input.
     */
    @Test
    public void testEmptyLookupTableAndNonEmptyInput() {
        final CharSequence[][] lookup = {};
        final LookupTranslator translator = new LookupTranslator(lookup);

        final StringBuilder actual = new StringBuilder();
        final int index = 0;
        final Writer out = new StringWriter();

        try {
            translator.translate("hello apache".toCharArray(), index, out);
        } catch (IOException e) {
            fail("IO exception during translation");
        }

        assertEquals("Expected 0 characters to be translated", 0, actual.length());
    }
}