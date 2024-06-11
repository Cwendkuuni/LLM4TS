package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity("123"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity("123"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName("123"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
        assertEquals("&#x41;", Entities.escape("A", new Document.OutputSettings()));
        assertEquals("&#x20;", Entities.escape(" ", new Document.OutputSettings()));
        assertEquals("&#xA0;", Entities.escape("\u00A0", new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("A", Entities.unescape("&#x41;"));
        assertEquals(" ", Entities.unescape("&#x20;"));
        assertEquals("\u00A0", Entities.unescape("&#xA0;"));
    }
}