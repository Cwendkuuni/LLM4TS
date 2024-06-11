package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = new CharSequence[][] {{"foo", "bar"}, {"hello", "world"}, {"one", "one"}, {"two", "two"}, {"three", "three"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        assertEquals("bar", translator.translate(new String("foo"), 0, new StringWriter()));
        assertEquals("world", translator.translate(new String("hello"), 0, new StringWriter()));
        assertEquals("", translator.translate(new String("one"), 0, new StringWriter()));
        assertEquals("two", translator.translate(new String("two"), 0, new StringWriter()));
        assertEquals("three", translator.translate(new String("three"), 0, new StringWriter()));

        assertEquals("", translator.translate(new String("invalid"), 0, new StringWriter()));
    }

    @Test
    public void testLookupTranslatorWithShortestAndLongest() {
        CharSequence[][] lookup = new CharSequence[][] {{"foo", "bar"}, {"hello", "world"}, {"one", "one"}, {"two", "two"}, {"three", "three"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "foobaronehellotwothree";
        StringWriter out = new StringWriter();

        translator.translate(new String(input), 0, out);

        assertEquals("baroneworldtwothree", out.toString());
    }

    @Test
    public void testLookupTranslatorWithNullLookup() {
        LookupTranslator translator = new LookupTranslator(null);

        String input = "foo";
        StringWriter out = new StringWriter();

        translator.translate(new String(input), 0, out);

        assertNull(out.toString());
    }

    @Test
    public void testLookupTranslatorWithEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});

        String input = "foo";
        StringWriter out = new StringWriter();

        translator.translate(new String(input), 0, out);

        assertEquals("", out.toString());
    }

    @Test
    public void testLookupTranslatorWithEmptyInput() {
        CharSequence[][] lookup = new CharSequence[][] {{"foo", "bar"}, {"hello", "world"}, {"one", "one"}, {"two", "two"}, {"three", "three"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "";
        StringWriter out = new StringWriter();

        translator.translate(new String(input), 0, out);

        assertEquals("", out.toString());
    }
}