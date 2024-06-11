package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.longest);
        assertEquals(2, translator.shortest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        String input = "foobazfoo";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertEquals(5, length);
        assertEquals("barquxbar", writer.toString());
        index += length;

        length = translator.translate(input, index, writer);
        assertEquals(0, length);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithEmptyLookup() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);

        StringWriter writer = new StringWriter();

        String input = "foo";
        int index = 0;

        int length = translator.translate(input, index, writer);
        assertEquals(0, length);
        assertEquals("", writer.toString());
        index += length;

        length = translator.translate(input, index, writer);
        assertEquals(3, length);
        assertEquals("foo", writer.toString());
        index += length;
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        int length = translator.translate(null, 0, writer);
        assertNull(writer.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        int length = translator.translate(CharSequence.class.cast(""), 0, writer);
        assertEquals(0, length);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        int length = translator.translate("foo".toString(), 0, null);
        assertEquals(3, length);
        assertEquals("foo", translator.lookupMap.get("foo"));
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNegativeIndex() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        translator.translate("foo".toString(), -1, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithIndexGreedierThanInput() throws IOException {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "foo";
        Writer writer = new StringWriter();

        translator.translate(input, input.length() + 1, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullLookup() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);

        StringWriter writer = new StringWriter();

        translator.translate("foo".toString(), 0, writer);
    }
}