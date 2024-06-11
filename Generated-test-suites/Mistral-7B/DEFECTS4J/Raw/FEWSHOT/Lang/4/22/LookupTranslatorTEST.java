package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(5, translator.longest);
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
    }

    @Test
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        assertThrows(NullPointerException.class, () -> translator.translate(null, 0, new StringWriter()));
    }

    @Test
    public void testTranslateWithEmptyCharSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharacteristicTest.EMPTY_CHAR_SEQUENCE, 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithShortSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithLongSequence() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobar";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithSequenceOutsideLookup() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "baz";
        int index = 0;
        assertEquals(0, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithSequencePartiallyMatched() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"foo", "baz"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobaz";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("baz", out.toString());
    }

    @Test
    public void testTranslateWithSequenceMatchedAtBeginning() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "barfoo";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithSequenceMatchedAtEnd() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobar";
        int index = 3;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithSequenceMatchedInMiddle() {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobarfoo";
        int index = 3;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithMultipleMatches() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"foo", "baz"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobazfoo";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("baz", out.toString());
    }

    @Test
    public void testTranslateWithPartialMatches() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"foo", "baz"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobazx";
        int index = 0;
        assertEquals(0, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithOverlappingMatches() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"foo", "baz"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foobaz";
        int index = 1;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("baz", out.toString());
    }

    @Test
    public void testTranslateWithEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        assertEquals(0, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithNullValueInLookup() {
        CharSequence[][] lookup = {{"foo", null}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        assertEquals(3, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMapContainsKey() {
        Map<CharSequence, CharSequence> map = new HashMap<>();
        map.put(CharacteristicTest.EMPTY_CHAR_SEQUENCE, CharacteristicTest.EMPTY_CHAR_SEQUENCE);
        LookupTranslator translator = new LookupTranslator(map.entrySet().toArray(new Map.Entry[0][]));
        StringWriter out = new StringWriter();
        CharSequence input = CharacteristicTest.EMPTY_CHAR_SEQUENCE;
        int index = 0;
        assertEquals(0, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMapDoesNotContainKey() {
        Map<CharSequence, CharSequence> map = new HashMap<>();
        LookupTranslator translator = new LookupTranslator(map.entrySet().toArray(new Map.Entry[0][]));
        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        assertEquals(0, translator.translate(input, index, out));
        assertEquals("", out.toString());
    }
}