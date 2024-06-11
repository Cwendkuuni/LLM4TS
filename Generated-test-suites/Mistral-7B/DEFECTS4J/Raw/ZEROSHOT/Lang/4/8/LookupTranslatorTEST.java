package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import org.junit.Test;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    /**
     * Test case for constructor and translate method with null lookup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullLookup() {
        new LookupTranslator(null);
    }

    /**
     * Test case for constructor and translate method with empty lookup.
     */
    @Test
    public void testEmptyLookup() {
        CharSequence[][] lookup = new CharSequence[0][];
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        CharSequence input = "test";
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals(0, translatedLength);
        assertEquals("", out.toString());
    }

    /**
     * Test case for constructor and translate method with lookup table of size 1.
     */
    @Test
    public void testLookupTableSizeOne() {
        CharSequence[][] lookup = new CharSequence[][] { { "test", "replacement" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        CharSequence input = "test";
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals(5, translatedLength);
        assertEquals("replacement", out.toString());
    }

    /**
     * Test case for constructor and translate method with lookup table of size > 1.
     */
    @Test
    public void testLookupTableSizeGreaterThanOne() {
        CharSequence[][] lookup = new CharSequence[][] { { "test", "replacement1" }, { "example", "replacement2" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();

        String input = "testexample";
        int index = 0;

        int translatedLength = translator.translate(CharSequenceUtils.subSequence(input, index, 4), index, out);
        assertEquals(5, translatedLength);
        assertEquals("replacement1", out.toString());

        index += 4;
        translatedLength = translator.translate(CharSequenceUtils.subSequence(input, index, 6), index, out);
        assertEquals(7, translatedLength);
        assertEquals("replacement2", out.toString());

        index += 6;
        translatedLength = translator.translate(input.subSequence(index), index, out);
        assertEquals(0, translatedLength);
        assertNull(out.toString());
    }
}