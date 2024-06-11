package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings(Entities.EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&#x1F466;", Entities.escape(Character.toString(0x1F466), out));
        assertEquals("foo", Entities.escape("foo", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals(Character.toString(0x1F466), Entities.unescape("&#x1F466;"));
        assertEquals("foo", Entities.unescape("foo"));
    }
}