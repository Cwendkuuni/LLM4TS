package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * JUnit Test File for LookupTranslator class.
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with null lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorNullLookupTable() {
        new LookupTranslator((CharSequence[][]) null);
    }

    /**
     * Test constructor with empty lookup table.
     */
    @Test
    public void testLookupTranslatorEmptyLookupTable() {
        CharSequence[][] lookup = new CharSequence[0][];
        new LookupTranslator(lookup);
    }

    /**
     * Test constructor with valid lookup table.
     */
    @Test
    public void testLookupTranslatorValidLookupTable() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apple", "pear"}};
        LookupTranslator translator = new LookupTranslator(lookup);
    }

    /**
     * Test translate method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        translator.translate(null, 0, new StringWriter());
    }

    /**
     * Test translate method with index out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateIndexOutOfBounds() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate("foobar".asCharSequence(), 5, new StringWriter());
    }

    /**
     * Test translate method with input less than lookup sequence length.
     */
    @Test
    public void testTranslateInputLessThanLookupSequenceLength() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals("foo", translator.translate("foo".asCharSequence(), 0, out));
        assertEquals("bar", translator.translate("bar".asCharSequence(), 0, out));
        assertNull(translator.translate("baz".asCharSequence(), 0, out));
    }

    /**
     * Test translate method with input equal to lookup sequence.
     */
    @Test
    public void testTranslateInputEqualToLookupSequence() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals("bar", translator.translate("foo".asCharSequence(), 3, out));
    }

    /**
     * Test translate method with input greater than lookup sequence length.
     */
    @Test
    public void testTranslateInputGreaterThanLookupSequenceLength() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertNull(translator.translate("foobar".asCharSequence(), 0, out));
    }

    /**
     * Test translate method with input containing multiple lookup sequences.
     */
    @Test
    public void testTranslateInputMultipleLookupSequences() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals("bar", translator.translate("foobar".asCharSequence(), 3, out));
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertNull(translator.translate(CharSequence.EMPTY, 0, out));
    }

    /**
     * Test translate method with input containing whitespace.
     */
    @Test
    public void testTranslateInputWithWhitespace() {
        CharSequence[][] lookup = {"foo".asCharSequence(), "bar ".asCharSequence()};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals("bar ", translator.translate("foo bar".asCharSequence(), 3, out));
    }
}