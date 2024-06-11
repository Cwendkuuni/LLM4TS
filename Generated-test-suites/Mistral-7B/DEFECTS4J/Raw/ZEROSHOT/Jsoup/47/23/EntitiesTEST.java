package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("amp"));
        assertFalse(entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertFalse(entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('&', entities.getCharacterByName("amp"));
        assertNull(entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "&lt;foo&gt;<bar>&amp;baz&quot;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt;&amp;amp;baz&amp;quot;";

        String result = entities.escape(input, outputSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt;&amp;amp;baz&amp;quot;";
        String expected = "&lt;foo&gt;&lt;bar&gt;&amp;baz\"";

        String result = entities.unescape(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = null; // not used in this test

        for (char c = ' '; c <= '~'; c++) {
            assertTrue(entities.canEncode(coreCharset, c, encoder));
        }

        char invalid = Character.MIN_VALUE;
        assertFalse(entities.canEncode(coreCharset, invalid, encoder));
    }
}