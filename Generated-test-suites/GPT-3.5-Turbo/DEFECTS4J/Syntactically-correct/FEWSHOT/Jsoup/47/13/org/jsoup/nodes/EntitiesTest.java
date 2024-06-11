package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("abc"));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape(" ", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("\"", Entities.escape("\"", out));
        assertEquals("&#xA0;", Entities.escape("\u00A0", out));
        assertEquals("&#65;", Entities.escape("A", out));
        assertEquals("&#x20AC;", Entities.escape("€", out));
        assertEquals("&#55357;&#56842;", Entities.escape("\uD83D\uDE0E", out));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals(" ", Entities.unescape("&nbsp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\u00A0", Entities.unescape("&#xA0;"));
        assertEquals("A", Entities.unescape("&#65;"));
        assertEquals("€", Entities.unescape("&#x20AC;"));
        assertEquals("\uD83D\uDE0E", Entities.unescape("&#55357;&#56842;"));
    }
}