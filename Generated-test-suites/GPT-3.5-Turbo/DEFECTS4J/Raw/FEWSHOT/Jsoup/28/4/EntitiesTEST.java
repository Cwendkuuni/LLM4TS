package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity("bar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName("bar"));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("&apos;", Entities.escape("'", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("&apos;", Entities.escape("'", new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(), Entities.EscapeMode.extended));
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(), Entities.EscapeMode.extended));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings(), Entities.EscapeMode.extended));
        assertEquals("&apos;", Entities.escape("'", new Document.OutputSettings(), Entities.EscapeMode.extended));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings(), Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
    }
}