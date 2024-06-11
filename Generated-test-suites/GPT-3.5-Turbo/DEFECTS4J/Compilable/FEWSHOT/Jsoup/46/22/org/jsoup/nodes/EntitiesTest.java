package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test known named entities
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test known base named entities
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test unknown base named entity
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test known named entities
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));

        // Test unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Test empty string
        assertEquals("", Entities.escape("", new Document.OutputSettings()));

        // Test basic escape characters
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));

        // Test non-standard escape character
        assertEquals("&#x1F642;", Entities.escape("\uD83D\uDE42", new Document.OutputSettings()));

        // Test multiple escape characters
        assertEquals("&amp;&lt;&gt;&quot;", Entities.escape("&<>\"", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        // Test empty string
        assertEquals("", Entities.unescape(""));

        // Test basic escape characters
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));

        // Test non-standard escape character
        assertEquals("\uD83D\uDE42", Entities.unescape("&#x1F642;"));

        // Test multiple escape characters
        assertEquals("&<>\"", Entities.unescape("&amp;&lt;&gt;&quot;"));
    }
}