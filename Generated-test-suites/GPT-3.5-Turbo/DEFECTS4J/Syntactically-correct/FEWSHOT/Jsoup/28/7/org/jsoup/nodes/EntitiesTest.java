
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

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
        assertEquals(Character.valueOf((char)0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char)0x0003E), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf((char)0x00026), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char)0x00027), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf((char)0x00022), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName("bar"));
    }

    @Test
    public void testEscape() {
        EscapeMode xhtmlEscapeMode = Entities.EscapeMode.xhtml;
        EscapeMode baseEscapeMode = Entities.EscapeMode.base;
        EscapeMode extendedEscapeMode = Entities.EscapeMode.extended;

        assertEquals("&quot;", Entities.escape("\"", xhtmlEscapeMode));
        assertEquals("&amp;", Entities.escape("&", xhtmlEscapeMode));
        assertEquals("&apos;", Entities.escape("'", xhtmlEscapeMode));
        assertEquals("&lt;", Entities.escape("<", xhtmlEscapeMode));
        assertEquals("&gt;", Entities.escape(">", xhtmlEscapeMode));

        assertEquals("&quot;", Entities.escape("\"", baseEscapeMode));
        assertEquals("&amp;", Entities.escape("&", baseEscapeMode));
        assertEquals("&apos;", Entities.escape("'", baseEscapeMode));
        assertEquals("&lt;", Entities.escape("<", baseEscapeMode));
        assertEquals("&gt;", Entities.escape(">", baseEscapeMode));

        assertEquals("&quot;", Entities.escape("\"", extendedEscapeMode));
        assertEquals("&amp;", Entities.escape("&", extendedEscapeMode));
        assertEquals("&apos;", Entities.escape("'", extendedEscapeMode));
        assertEquals("&lt;", Entities.escape("<", extendedEscapeMode));
        assertEquals("&gt;", Entities.escape(">", extendedEscapeMode));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
    }
    
    @Test
    public void testUnescapeWithInvalidInput() {
        assertEquals("", Entities.unescape("foo"));
        assertEquals("", Entities.unescape("bar"));
        assertEquals("", Entities.unescape(""));
    }
}
