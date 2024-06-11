package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("This is a foo"), 0, new StringWriter()));
        assertEquals("fruit", translator.translate(new String("This is some apples"), 4, new StringWriter()));
        assertNull(translator.translate(new String("This is not in the lookup table"), 0, new StringWriter()));
    }

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        String input = "This is a test for empty lookup table";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
    }

    @Test
    public void testShortestKey() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}, {"one", "two"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for shortest key";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
        translator.translate(new String(input), 2, writer);
        assertEquals("two", writer.toString());
    }

    @Test
    public void testLongestKey() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}, {"this_is_a_long_key", "long_value"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for longest key";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
        translator.translate(new String(input), input.length() - 23, writer);
        assertEquals("long_value", writer.toString());
    }

    @Test
    public void testKeyNotInLookupTable() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for key not in lookup table";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
        translator.translate(new String(input), input.length() - 9, writer);
        assertNull(writer.toString());
    }

    @Test
    public void testLookupTableWithMultipleMatches() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"foo", "baz"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for multiple matches in lookup table";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 3, writer);
        assertEquals("baz", writer.toString());
    }

    @Test
    public void testLookupTableWithEmptyStringKey() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"", "empty_value"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for empty string key";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
        translator.translate(new String(input), input.length(), writer);
        assertEquals("empty_value", writer.toString());
    }

    @Test
    public void testLookupTableWithNullInput() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();
        assertNull(translator.translate(null, 0, writer));
        assertNull(translator.translate(new String(), 0, writer));
    }

    @Test
    public void testLookupTableWithInputLongerThanLookupTable() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for input longer than lookup table";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
    }

    @Test
    public void testLookupTableWithInputShorterThanShortestKey() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"apples", "fruit"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "This is a test for input shorter than shortest key";
        StringWriter writer = new StringWriter();
        translator.translate(new String(input), 0, writer);
        assertEquals(input, writer.toString());
    }
}