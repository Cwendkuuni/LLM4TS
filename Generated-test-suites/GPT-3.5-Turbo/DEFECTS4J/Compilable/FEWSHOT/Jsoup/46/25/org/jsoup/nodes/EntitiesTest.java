package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(null, Entities.getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&#x00022;", Entities.escape("\"", out));
        assertEquals("hello world", Entities.escape("hello world", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&invalid;", Entities.unescape("&invalid;"));
    }

    @Test
    public void testEscapeMode_xhtml() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Map<Character, String> map = escapeMode.getMap();
        assertEquals(4, map.size());
        assertTrue(map.containsKey('<'));
        assertTrue(map.containsKey('>'));
        assertTrue(map.containsKey('&'));
        assertTrue(map.containsKey('"'));
        assertEquals("lt", map.get('<'));
        assertEquals("gt", map.get('>'));
        assertEquals("amp", map.get('&'));
        assertEquals("quot", map.get('"'));
    }

    @Test
    public void testEscapeMode_base() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
        Map<Character, String> map = escapeMode.getMap();
        assertEquals(4, map.size());
        assertTrue(map.containsKey('<'));
        assertTrue(map.containsKey('>'));
        assertTrue(map.containsKey('&'));
        assertTrue(map.containsKey('"'));
        assertEquals("lt", map.get('<'));
        assertEquals("gt", map.get('>'));
        assertEquals("amp", map.get('&'));
        assertEquals("quot", map.get('"'));
    }

    @Test
    public void testEscapeMode_extended() {
        Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
        Map<Character, String> map = escapeMode.getMap();
        assertEquals(4, map.size());
        assertTrue(map.containsKey('<'));
        assertTrue(map.containsKey('>'));
        assertTrue(map.containsKey('&'));
        assertTrue(map.containsKey('"'));
        assertEquals("lt", map.get('<'));
        assertEquals("gt", map.get('>'));
        assertEquals("amp", map.get('&'));
        assertEquals("quot", map.get('"'));
    }
}