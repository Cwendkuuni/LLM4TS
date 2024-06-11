package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit test cases for LookupTranslator class
 */
public class LookupTranslatorTest {

    /**
     * Test LookupTranslator constructor with empty lookup table
     */
    @Test
    public void testLookupTranslatorEmpty() {
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertNull(translator.lookupMap);
    }

    /**
     * Test LookupTranslator constructor with lookup table of size 0
     */
    @Test
    public void testLookupTranslatorSizeZero() {
        CharSequence[][] lookup = new CharSequence[0][];
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertNull(translator.lookupMap);
    }

    /**
     * Test LookupTranslator constructor with lookup table of size 1
     */
    @Test
    public void testLookupTranslatorSizeOne() {
        CharSequence[][] lookup = new CharSequence[][] { {"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(3, translator.shortest);
        assertEquals(3, translator.longest);
        Map<CharSequence, CharSequence> expectedMap = new HashMap<CharSequence, CharSequence>() {{
            put(CharSequence.valueOf("foo"), CharSequence.valueOf("bar"));
        }};
        assertEquals(expectedMap, translator.lookupMap);
    }

    /**
     * Test LookupTranslator constructor with lookup table of size > 1
     */
    @Test
    public void testLookupTranslatorSizeMoreThanOne() {
        CharSequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(3, translator.shortest);
        assertEquals(7, translator.longest);
        Map<CharSequence, CharSequence> expectedMap = new HashMap<CharSequence, CharSequence>() {{
            put(CharSequence.valueOf("foo"), CharSequence.valueOf("bar"));
            put(CharSequence.valueOf("baz"), CharSequence.valueOf("qux"));
            put(CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar"));
        }};
        assertEquals(expectedMap, translator.lookupMap);
    }

    /**
     * Test translate method with empty input
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with input shorter than lookup
     */
    @Test
    public void testTranslateInputShorterThanLookup() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("foo"), 0, out));
        assertEquals("bar", out.toString());
    }

    /**
     * Test translate method with input longer than lookup
     */
    @Test
    public void testTranslateInputLongerThanLookup() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("foobar"), 0, out));
        assertEquals("bar", out.toString());
    }

    /**
     * Test translate method with input matching lookup
     */
    @Test
    public void testTranslateInputMatchesLookup() throws IOException {
        Charsequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("foo"), 0, out));
        assertEquals("bar", out.toString());
    }

    /**
     * Test translate method with input not matching any lookup
     */
    @Test
    public void testTranslateInputNotMatchesAnyLookup() throws IOException {
        Charsequence[][] lookup = new CharSequence[][] {
            {CharSequence.valueOf("foo"), CharSequence.valueOf("bar")},
            {CharSequence.valueOf("baz"), CharSequence.valueOf("qux")},
            {CharSequence.valueOf("quux"), CharSequence.valueOf("quuxbar")}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        Writer out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.valueOf("quuz"), 0, out));
        assertEquals("", out.toString());
    }

}