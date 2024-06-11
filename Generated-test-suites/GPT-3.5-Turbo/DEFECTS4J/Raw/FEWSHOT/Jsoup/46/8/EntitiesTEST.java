package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("nbsp"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity("bar"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity("bar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 0x0003E), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 0x000A0), Entities.getCharacterByName("nbsp"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName("bar"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
        assertEquals("nbsp;", Entities.escape("\u00a0", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }
}