package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setUp() {
        translator = new LookupTranslator(new CharSequence[][] {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"},
                {"d", "D"}
        });
    }

    @Test
    public void testConstructor() {
        HashMap<CharSequence, CharSequence> lookupMap = translator.getLookupMap();
        assertEquals(4, lookupMap.size());
        assertEquals("A", lookupMap.get("a"));
        assertEquals("B", lookupMap.get("b"));
        assertEquals("C", lookupMap.get("c"));
        assertEquals("D", lookupMap.get("d"));
    }

    @Test
    public void testTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int index = translator.translate("Hello world", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());

        writer = new StringWriter();
        index = translator.translate("aBCD", 1, writer);
        assertEquals(1, index);
        assertEquals("A", writer.toString());

        writer = new StringWriter();
        index = translator.translate("xYz", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InputTooShort() throws IOException {
        StringWriter writer = new StringWriter();
        int index = translator.translate("a", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_InputTooLong() throws IOException {
        StringWriter writer = new StringWriter();
        int index = translator.translate("abcde", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NullInput() throws IOException {
        StringWriter writer = new StringWriter();
        int index = translator.translate(null, 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NullWriter() throws IOException {
        int index = translator.translate("aBCD", 1, null);
        assertEquals(1, index);
    }
    
    @Test
    public void testTranslate_EmptyLookupMap() throws IOException {
        translator = new LookupTranslator();
        StringWriter writer = new StringWriter();
        int index = translator.translate("aBCD", 1, writer);
        assertEquals(1, index);
        assertEquals("", writer.toString());
    }

    @Test
    public void testEscape() {
        CharSequence escaped = translator.escape("a");
        assertEquals("A", escaped);

        escaped = translator.escape("b");
        assertEquals("B", escaped);

        escaped = translator.escape("c");
        assertEquals("C", escaped);

        escaped = translator.escape("d");
        assertEquals("D", escaped);

        escaped = translator.escape("e");
        assertNull(escaped);
    }

    @Test
    public void testGetLookupMap() {
        HashMap<CharSequence, CharSequence> lookupMap = translator.getLookupMap();
        assertEquals(4, lookupMap.size());
        assertTrue(lookupMap.containsKey("a"));
        assertTrue(lookupMap.containsKey("b"));
        assertTrue(lookupMap.containsKey("c"));
        assertTrue(lookupMap.containsKey("d"));
    }
}