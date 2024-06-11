package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Parser;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(null, Entities.getCharacterByName("foo"));
        assertEquals(null, Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&nbsp;", Entities.escape("\u00a0", out));
        assertEquals("&#x1a;", Entities.escape("\u001a", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("lt", Entities.unescape("&lt;"));
        assertEquals("gt", Entities.unescape("&gt;"));
        assertEquals("amp", Entities.unescape("&amp;"));
        assertEquals("quot", Entities.unescape("&quot;"));
        assertEquals("&foo;", Entities.unescape("&foo;"));
        assertEquals("&foo", Entities.unescape("&foo"));
    }
}