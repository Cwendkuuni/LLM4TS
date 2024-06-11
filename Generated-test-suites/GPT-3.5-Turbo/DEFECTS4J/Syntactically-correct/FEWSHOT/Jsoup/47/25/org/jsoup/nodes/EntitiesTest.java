package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("hello"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("hello"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("hello"));
    }

    @Test
    public void testEscape() {
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
        assertEquals("&lt;", Entities.escape("<", outputSettings));
    }

    @Test
    public void testUnescape() {
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("<", Entities.unescape("&lt;"));
    }

    @Test
    public void testEscape_stripsLeadingWhite() {
        assertEquals(" foo", Entities.escape(" foo", outputSettings));
        assertEquals("foo", Entities.escape("foo", outputSettings));
    }

    @Test
    public void testEscape_normalizesWhite() {
        assertEquals("foo bar", Entities.escape("foo   bar", outputSettings));
        assertEquals("foo bar", Entities.escape("foo \tbar", outputSettings));
    }

    @Test
    public void testEscape_handlesSupplementaryChars() {
        assertEquals("&#x1D306;", Entities.escape("\uD834\uDF06", outputSettings));
    }

    @Test
    public void testUnescape_handlesSupplementaryChars() {
        assertEquals("\uD834\uDF06", Entities.unescape("&#x1D306;"));
    }

    private static Document.OutputSettings outputSettings = new Document.OutputSettings();

    static {
        outputSettings.escapeMode(Document.EscapeMode.xhtml);
    }
}