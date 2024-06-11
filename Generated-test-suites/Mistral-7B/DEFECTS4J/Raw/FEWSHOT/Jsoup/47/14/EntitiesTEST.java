package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("x"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("x"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;foo&gt;", Entities.escape("<foo>", Entities.EscapeMode.base));
        assertEquals("&amp;lt;foo&amp;gt;", Entities.escape("<foo>", Entities.EscapeMode.extended));
        assertEquals("&x26;lt;foo&x26;gt;", Entities.escape("<foo>", Entities.EscapeMode.extended, true));
    }

    @Test
    public void testUnescape() {
        assertEquals("<foo>", Entities.unescape("&lt;foo&gt;"));
        assertEquals("&amp;lt;foo&amp;gt;", Entities.unescape("&amp;lt;foo&amp;gt;"));
        assertEquals("&#x26;lt;foo&#x26;gt;", Entities.unescape("&#x26;lt;foo&#x26;gt;"));
    }
}