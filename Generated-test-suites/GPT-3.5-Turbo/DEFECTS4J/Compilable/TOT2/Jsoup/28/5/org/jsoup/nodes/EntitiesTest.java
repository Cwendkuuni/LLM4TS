package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // Typical use case
        assertFalse(Entities.isNamedEntity("xyz")); // Edge case
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // Typical use case
        assertNull(Entities.getCharacterByName("xyz")); // Edge case
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&amp;&lt;&gt;", Entities.escape("&<>", out)); // Typical use case
        assertEquals("&amp;&lt;&gt;", Entities.escape("&<>", null, Entities.EscapeMode.xhtml)); // Edge case
    }

    @Test
    public void testUnescape() {
        assertEquals("&<>", Entities.unescape("&amp;&lt;&gt;")); // Typical use case
        assertEquals("&xyz;", Entities.unescape("&xyz;")); // Edge case
    }

    // Add more test cases for additional methods if needed

}