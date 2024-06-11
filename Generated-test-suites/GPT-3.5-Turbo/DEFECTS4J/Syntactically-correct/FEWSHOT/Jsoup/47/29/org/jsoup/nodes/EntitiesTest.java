
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("notEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("notEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x3C), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("notEntity"));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&"));
        assertEquals("&lt;", Entities.escape("<"));
        assertEquals("&gt;", Entities.escape(">"));
        assertEquals("&quot;", Entities.escape("\""));
        assertEquals("&nbsp;", Entities.escape("\u00A0"));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
    }
}
