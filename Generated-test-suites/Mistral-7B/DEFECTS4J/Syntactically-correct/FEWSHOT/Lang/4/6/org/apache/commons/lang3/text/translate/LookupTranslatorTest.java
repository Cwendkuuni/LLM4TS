package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foo"), 0, null));
        assertEquals("qux", translator.translate(new String("baz"), 0, null));
        assertNull(translator.translate(new String("quux"), 0, null));
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNull(translator.translate(new String("foo"), 0, null));
    }

    @Test
    public void testTranslateWithShortestSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobar";
        assertEquals(6, translator.translate(new String(input), 0, null).length());
        assertEquals("barbar", translator.translate(new String(input), 3, null));
    }

    @Test
    public void testTranslateWithLongestSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobarfoobar";
        assertEquals(12, translator.translate(new String(input), 0, null).length());
        assertEquals("barbar", translator.translate(new String(input), 6, null));
        assertEquals("barbar", translator.translate(new String(input), 12, null));
    }

    @Test
    public void testTranslateWithMiddleSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobarfoobaz";
        assertEquals(8, translator.translate(new String(input), 3, null).length());
        assertEquals("qux", translator.translate(new String(input), 6, null));
    }

    @Test
    public void testTranslateWithPartialSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobaz";
        assertNull(translator.translate(new String(input), 2, null));
        assertEquals("baz", translator.translate(new String(input), 1, null));
    }

    @Test
    public void testTranslateWithEmptySequence() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "";
        assertNull(translator.translate(new String(input), 0, null));
    }
}