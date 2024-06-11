package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Entities.EscapeMode;

import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {
    
    // Test case for isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
    }
    
    // Test case for isBaseNamedEntity() method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }
    
    // Test case for getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 34), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf((char) 38), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 60), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 62), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("abc"));
    }
    
    // Test case for escape() method
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.setEscapeMode(EscapeMode.xhtml);
        
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&#x26;", Entities.escape("\u0026", out));
    }
    
    // Test case for unescape() method
    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        
        assertEquals("©", Entities.unescape("&copy;"));
        assertEquals("®", Entities.unescape("&reg;"));
    }
}