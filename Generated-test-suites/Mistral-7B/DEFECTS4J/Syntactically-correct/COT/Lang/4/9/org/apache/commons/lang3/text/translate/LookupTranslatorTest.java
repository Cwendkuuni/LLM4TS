package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequencePair;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * JUnit test cases for the LookupTranslator class
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with empty lookup table
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequencePair[0][]);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertNull(translator.lookupMap);
    }

    /**
     * Test constructor with valid lookup table
     */
    @Test
    public void testValidLookupTable() {
        CharSequencePair[] lookup = {new CharSequencePair("key1", "value1"), new CharSequencePair("key2", "value2")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(2, translator.longest);
        assertEquals(2, translator.lookupMap.size());
        assertEquals("value1", translator.lookupMap.get("key1"));
        assertEquals("value2", translator.lookupMap.get("key2"));
    }

    /**
     * Test translate method with exact match
     */
    @Test
    public void testTranslateExactMatch() {
        CharSequencePair[] lookup = {new CharSequencePair("test", "result")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("testtesttest"), 0, out));
        assertEquals("resultresultresult", out.toString());
    }

    /**
     * Test translate method with prefix match
     */
    @Test
    public void testTranslatePrefixMatch() {
        CharSequencePair[] lookup = {new CharSequencePair("test", "result")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("testprefixresult"), 0, out));
        assertEquals("resultresultresult", out.toString());
    }

    /**
     * Test translate method with suffix match
     */
    @Test
    public void testTranslateSuffixMatch() {
        CharSequencePair[] lookup = {new CharSequencePair("result", "value")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(5, translator.translate(new String("prefixresultvalue"), 5, out));
        assertEquals("value", out.toString());
    }

    /**
     * Test translate method with no match
     */
    @Test
    public void testTranslateNoMatch() {
        CharSequencePair[] lookup = {new CharSequencePair("test", "result")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new String("prefixvalue"), 0, out));
        assertEquals("", out.toString());
    }

    /**
     * Test translate method with index out of bounds
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateIndexOutOfBounds() {
        CharSequencePair[] lookup = {new CharSequencePair("test", "result")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        translator.translate(new String("test"), 4, out);
    }

    /**
     * Test translate method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequencePair[0][]);
        StringWriter out = new StringWriter();
        translator.translate(null, 0, out);
    }

    /**
     * Test translate method with null writer
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriter() {
        CharSequencePair[] lookup = {new CharSequencePair("test", "result")};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate("test", 0, null);
    }
}