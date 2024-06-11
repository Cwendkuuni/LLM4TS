package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName(""));
    }
    
    @Test
    public void testEscape() {
        // Test for xhtml mode
        Entities.EscapeMode mode = Entities.EscapeMode.xhtml;
        Map<Character, String> map = mode.getMap();
        
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&nbsp;", Entities.escape("\u00A0", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&amp;b", Entities.escape("a&b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&lt;b", Entities.escape("a<b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&gt;b", Entities.escape("a>b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&quot;b", Entities.escape("a\"b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&#x1f4a9;", Entities.escape("\uD83D\uDCA9", new Document.OutputSettings().escapeMode(mode)));
        
        // Test for base mode
        mode = Entities.EscapeMode.base;
        map = mode.getMap();
                
        assertEquals("<", Entities.escape("<", new Document.OutputSettings().escapeMode(mode)));
        assertEquals(">", Entities.escape(">", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("\"", Entities.escape("\"", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&", Entities.escape("&", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("\u00A0", Entities.escape("\u00A0", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&b", Entities.escape("a&b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a<b", Entities.escape("a<b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a>b", Entities.escape("a>b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a\"b", Entities.escape("a\"b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&#x1f4a9;", Entities.escape("\uD83D\uDCA9", new Document.OutputSettings().escapeMode(mode)));
        
        // Test for extended mode
        mode = Entities.EscapeMode.extended;
        map = mode.getMap();
        
        assertEquals("<", Entities.escape("<", new Document.OutputSettings().escapeMode(mode)));
        assertEquals(">", Entities.escape(">", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("\"", Entities.escape("\"", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&", Entities.escape("&", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("\u00A0", Entities.escape("\u00A0", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a&b", Entities.escape("a&b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a<b", Entities.escape("a<b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a>b", Entities.escape("a>b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("a\"b", Entities.escape("a\"b", new Document.OutputSettings().escapeMode(mode)));
        assertEquals("&#x1f4a9;", Entities.escape("\uD83D\uDCA9", new Document.OutputSettings().escapeMode(mode)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals("\uD83D\uDCA9", Entities.unescape("&#x1f4a9;"));
        assertEquals("a&b", Entities.unescape("a&amp;b"));
        assertEquals("a<b", Entities.unescape("a&lt;b"));
        assertEquals("a>b", Entities.unescape("a&gt;b"));
        assertEquals("a\"b", Entities.unescape("a&quot;b"));
        assertEquals("&#x1f4a9;", Entities.unescape("&#x1f4a9;"));
    }
    
}