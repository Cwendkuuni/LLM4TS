package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' should have character value '" + character + "'", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> base = Entities.base;
        for (Map.Entry<String, Character> entry : base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' should be in base set", Entities.isBaseNamedEntity(name));
            assertEquals("Named entity '" + name + "' should have character value '" + character + "'", character, Entities.getCharacterByName(name));
        }

        // test for unknown named entity
        assertFalse("Unknown named entity should not be in base set", Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals("Named entity '" + name + "' should have character value '" + character + "'", character, Entities.getCharacterByName(name));
        }

        // test for unknown named entity
        assertNull("Unknown named entity should return null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        String input = "&lt;foo&amp;bar&gt; <b>hello</b> world! &nbsp;";
        String expectedOutput = "&amp;lt;foo&amp;amp;amp;amp;amp;amp;lt; &amp;amp;amp;amp;amp;lt;b&amp;gt;hello&amp;lt;/b&amp;gt; &amp;nbsp;";
        assertEquals("Escaped output should match expected output", expectedOutput, Entities.escape(input, out));

        // test for empty string
        assertEquals("Empty string should not be modified", "", Entities.escape("", out));

        // test for null input
        assertNull("Null input should return null", Entities.escape(null, out));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;amp;amp;amp;amp;amp;lt; &amp;amp;amp;amp;amp;lt;b&gt;hello&amp;lt;/b&gt; &amp;nbsp;";
        String expectedOutput = "&lt;foo&gt; <b>hello</b> &nbsp;";
        assertEquals("Unescaped output should match expected output", expectedOutput, Entities.unescape(input));

        // test for empty string
        assertEquals("Empty string should not be modified", "", Entities.unescape(""));

        // test for null input
        assertNull("Null input should return null", Entities.unescape(null));
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in canEncode test
        for (int codePoint = 0; codePoint <= 0xFFFF; codePoint++) {
            char c = (char) codePoint;
            assertTrue("Character '" + c + "' should be encodable", Entities.canEncode(coreCharset, c, encoder));
        }

        // test for surrogate pair
        char highSurrogate = '\uD800';
        char lowSurrogate = '\uDC00';
        assertFalse("Surrogate pair should not be encodable", Entities.canEncode(coreCharset, highSurrogate, encoder));
        assertFalse("Surrogate pair should not be encodable", Entities.canEncode(coreCharset, lowSurrogate, encoder));
        assertFalse("Surrogate pair should not be encodable", Entities.canEncode(coreCharset, highSurrogate + lowSurrogate, encoder));
    }
}