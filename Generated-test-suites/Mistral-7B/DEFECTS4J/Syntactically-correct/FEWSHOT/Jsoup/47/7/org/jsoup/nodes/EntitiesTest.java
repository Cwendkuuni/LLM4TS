package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        // Add more test cases for other named entities
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        // Add more test cases for other base named entities
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        // Add more test cases for other named entities
    }

    @Test
    public void testEscape() {
        String input = "&lt;foo&gt;<bar>&quot;<baz>";
        assertEquals("&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt;&amp;quot;&amp;#x62;&amp;#x61;&amp;#x64;", Entities.escape(input, Document.OutputSettings.xhtml()));

        input = "&amp;lt;foo&gt;<bar>&quot;<baz>";
        assertEquals(input, Entities.escape(input, Document.OutputSettings.base()));
        // Add more test cases for different inputs and OutputSettings
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt;&amp;quot;&#x62;&#x61;&#x64;";
        assertEquals("&lt;foo&gt;&lt;bar&gt;&quot;bat", Entities.unescape(input));

        input = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;gt;&amp;quot;&#x62;&#x61;&#x64;";
        assertEquals(input, Entities.unescape(input, true));
        // Add more test cases for different inputs and strictness
    }
}