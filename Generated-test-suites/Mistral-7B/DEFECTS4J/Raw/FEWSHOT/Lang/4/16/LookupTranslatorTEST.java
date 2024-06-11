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
        CharSequence[][] lookup = new CharSequence[][]{
            new String[]{"foo", "bar"},
            new String[]{"baz", "qux"},
            new String[]{"quux", "corge"}};

        LookupTranslator translator = new LookupTranslator(lookup);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("foo", "bar");
        expectedMap.put("baz", "qux");
        expectedMap.put("quux", "corge");

        assertEquals(expectedMap, translator.lookupMap);
        assertEquals(3, translator.shortest);
        assertEquals(3, translator.longest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{
            new String[]{"foo", "bar"},
            new String[]{"baz", "qux"},
            new String[]{"quux", "corge"}};

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        // Test empty input
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, writer));
        assertEquals(String.EMPTY, writer.toString());

        // Test input that doesn't match any lookup sequence
        assertEquals(0, translator.translate(new String("hello"), 0, writer));
        assertEquals(String.EMPTY, writer.toString());

        // Test input that matches a lookup sequence
        assertEquals(3, translator.translate(new String("foo"), 0, writer));
        assertEquals("bar", writer.toString());
        writer.getBuffer().setLength(0);

        // Test input that matches a lookup sequence with an offset
        assertEquals(3, translator.translate(new String("foobar"), 0, writer));
        assertEquals("bar", writer.toString());
        writer.getBuffer().setLength(0);

        // Test input that matches a lookup sequence with a length longer than the input
        assertEquals(3, translator.translate(new String("foobar"), 1, writer));
        assertEquals("bar", writer.toString());
        writer.getBuffer().setLength(0);

        // Test input that matches a lookup sequence with a length shorter than the input
        assertEquals(2, translator.translate(new String("foobaz"), 0, writer));
        assertEquals("qux", writer.toString());
        writer.getBuffer().setLength(0);

        // Test input that matches multiple lookup sequences
        assertEquals(3, translator.translate(new String("quuxfooqux"), 0, writer));
        assertEquals("corgebar", writer.toString());
        writer.getBuffer().setLength(0);

        // Test input that matches multiple lookup sequences, with an offset
        assertEquals(2, translator.translate(new String("quuxfooqux"), 1, writer));
        assertEquals("corge", writer.toString());
        writer.getBuffer().setLength(0);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{
            new String[]{"foo", "bar"},
            new String[]{"baz", "qux"},
            new String[]{"quux", "corge"}};

        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();

        assertNull(translator.translate(null, 0, writer));
        assertEquals(String.EMPTY, writer.toString());
    }

    @Test
    public void testTranslateWithNullLookup() {
        LookupTranslator translator = new LookupTranslator(null);

        HashMap<CharSequence, CharSequence> expectedEmptyMap = new HashMap<>();

        assertEquals(expectedEmptyMap, translator.lookupMap);
        assertEquals(-1, translator.shortest);
        assertEquals(-1, translator.longest);
    }
}