package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

    /**
     * Test empty constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new LookupTranslator((CharSequence[][]) null);
    }

    /**
     * Test constructor with empty lookup table.
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, new StringWriter()));
    }

    /**
     * Test constructor with valid lookup table.
     */
    @Test
    public void testValidLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "abcxyz";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("defpqr", out.toString());
        assertEquals(6, translatedLength);
        index += translatedLength;

        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test constructor with lookup table containing duplicate keys.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKeysInLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"abc", "pqr"}};
        new LookupTranslator(lookup);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}});
        StringWriter out = new StringWriter();
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, out));
    }

    /**
     * Test translate method with index out of bounds.
     */
    @Test
    public void testIndexOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}});
        StringWriter out = new StringWriter();
        String input = "bar";
        int index = input.length();

        int translatedLength = translator.translate(input, index, out);
        assertEquals(0, translatedLength);
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with input longer than longest lookup value.
     */
    @Test
    public void testInputLongerThanLongestLookupValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"abc", "def"}, {"xyz", "pqr"}});
        StringWriter out = new StringWriter();
        String input = "abcxyz";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("defpqr", out.toString());
        assertEquals(6, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        input += "w";
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with input shorter than shortest lookup value.
     */
    @Test
    public void testInputShorterThanShortestLookupValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{"abcdef", "123456"}, {"xyz", "pqr"}});
        StringWriter out = new StringWriter();
        String input = "xyz";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("pqr", out.toString());
        assertEquals(3, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with lookup table containing empty values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValuesInLookupTable() {
        CharSequence[][] lookup = {{"abc", ""}, {"xyz", "pqr"}};
        new LookupTranslator(lookup);
    }

    /**
     * Test translate method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}});
        StringWriter out = new StringWriter();
        translator.translate(null, 0, out);
    }

    /**
     * Test translate method with null writer.
     */
    @Test(expected = NullPointerException.class)
    public void testNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}});
        translator.translate(StringUtils.EMPTY, 0, null);
    }

    /**
     * Test translate method with IOException.
     */
    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}});
        StringWriter out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        translator.translate(StringUtils.EMPTY, 0, out);
    }

    /**
     * Test translate method with multiple possible matches.
     */
    @Test
    public void testTranslateWithMultipleMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, "foo"}, {"bar", "baz"}});
        StringWriter out = new StringWriter();
        String input = "bar";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("baz", out.toString());
        assertEquals(3, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with empty lookup table.
     */
    @Test
    public void testTranslateWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        String input = "abcxyz";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        input += "w";
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with large input.
     */
    @Test
    public void testTranslateWithLargeInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.repeat("abc", 10), "def"}, {"xyz", "pqr"}});
        StringWriter out = new StringWriter();
        String input = StringUtils.repeat("abcxyz", 100);

        int index = 0;
        for (int i = 0; i < 100; i += 3) {
            int translatedLength = translator.translate(input, index, out);
            assertEquals("def", out.toString());
            index += translatedLength;
            out.getBuffer().setLength(0);
        }

        // final translation
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with empty lookup table and empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, out).toString());
    }

    /**
     * Test translate method with empty lookup table and non-empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndNonEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        String input = "abc";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with null lookup table.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        StringWriter out = new StringWriter();
        String input = "abcxyz";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        input += "w";
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with empty lookup table and empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndEmptyInputNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, null).toString());
    }

    /**
     * Test translate method with empty lookup table and empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndEmptyInputWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, out).toString());
    }

    /**
     * Test translate method with empty lookup table and non-empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndNonEmptyInputNullWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        String input = "abc";

        int index = 0;
        int translatedLength = translator.translate(input, index, null);
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, null);
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with empty lookup table and non-empty input.
     */
    @Test
    public void testTranslateWithEmptyLookupTableAndNonEmptyInputWriter() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        String input = "abc";

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with lookup table containing values longer than available space.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithLookupTableContainingValuesLongerThanAvailableSpace() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, StringUtils.repeat("a", 1001)}});
        StringWriter out = new StringWriter();
        String input = StringUtils.repeat("a", 1000);

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        input += "a";
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    /**
     * Test translate method with lookup table containing values longer than available space and empty input.
     */
    @Test
    public void testTranslateWithLookupTableContainingValuesLongerThanAvailableSpaceAndEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, StringUtils.repeat("a", 1001)}});
        StringWriter out = new StringWriter();
        assertEquals("", translator.translate(StringUtils.EMPTY, 0, out).toString());
    }

    /**
     * Test translate method with lookup table containing values longer than available space and non-empty input.
     */
    @Test
    public void testTranslateWithLookupTableContainingValuesLongerThanAvailableSpaceAndNonEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {{StringUtils.EMPTY, StringUtils.repeat("a", 1001)}});
        StringWriter out = new StringWriter();
        String input = StringUtils.repeat("a", 1000);

        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        index += translatedLength;
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);

        input += "a";
        translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }
}