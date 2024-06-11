
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertFalse(Entities.isNamedEntity("invalid")); // error scenario
        assertTrue(Entities.isNamedEntity("amp")); // typical use case
        assertFalse(Entities.isNamedEntity(null)); // error scenario
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("invalid")); // error scenario
        assertTrue(Entities.isBaseNamedEntity("amp")); // typical use case
        assertFalse(Entities.isBaseNamedEntity(null)); // error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertNull(Entities.getCharacterByName("invalid")); // error scenario
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // typical use case
        assertNull(Entities.getCharacterByName(null)); // error scenario
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml))); // typical use case
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml))); // typical use case
        assertEquals("abc", Entities.escape("abc", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml))); // typical use case
        assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml))); // error scenario
    }

    @Test
    public void testUnescape() {
        assertEquals("abc", Entities.unescape("abc")); // typical use case
        assertEquals("abc", Entities.unescape("abc", true)); // typical use case
        assertEquals("&lt;", Entities.unescape("&lt;")); // typical use case
        assertEquals("&lt;", Entities.unescape("&lt;", false)); // typical use case
    }
}
