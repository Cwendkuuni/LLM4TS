package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Entities.EscapeMode;

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
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.charset("UTF-8");

        assertEquals("lt", Entities.escape("<", settings));
        assertEquals("gt", Entities.escape(">", settings));
        assertEquals("amp", Entities.escape("&", settings));
        assertEquals("apos", Entities.escape("'", settings));
        assertEquals("quot", Entities.escape("\"", settings));
        assertEquals("&#60;", Entities.escape("&#60;", settings));
        assertEquals("&#62;", Entities.escape("&#62;", settings));
        assertEquals("&amp;", Entities.escape("&amp;", settings));
        assertEquals("&quot;", Entities.escape("&quot;", settings));
        assertEquals("&apos;", Entities.escape("&apos;", settings));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("lt"));
        assertEquals(">", Entities.unescape("gt"));
        assertEquals("&", Entities.unescape("amp"));
        assertEquals("'", Entities.unescape("apos"));
        assertEquals("\"", Entities.unescape("quot"));
        assertEquals("&#60;", Entities.unescape("&#60;"));
        assertEquals("&#62;", Entities.unescape("&#62;"));
        assertEquals("&amp;", Entities.unescape("&amp;"));
        assertEquals("&quot;", Entities.unescape("&quot;"));
        assertEquals("&apos;", Entities.unescape("&apos;"));
    }    
}