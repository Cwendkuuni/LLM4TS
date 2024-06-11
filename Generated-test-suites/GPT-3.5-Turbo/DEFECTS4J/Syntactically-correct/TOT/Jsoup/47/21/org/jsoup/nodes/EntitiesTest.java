package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("nbsp"));
        
        // Edge case
        assertFalse(Entities.isNamedEntity("abcd"));
        
        // Error scenario
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge case
        assertFalse(Entities.isBaseNamedEntity("abcd"));
        
        // Error scenario
        assertFalse(Entities.isBaseNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        
        // Edge case
        assertNull(Entities.getCharacterByName("abcd"));
        
        // Error scenario
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscape() {
        // Typical use case
        assertEquals("&lt;&gt;&quot;&amp;", Entities.escape("<>\"&", null));
        
        // Edge case
        assertEquals("", Entities.escape("", null));
        
        // Error scenario
        assertNull(Entities.escape(null, null));
    }
    
    @Test
    public void testUnescape() {
        // Typical use case
        assertEquals("<>\"&", Entities.unescape("&lt;&gt;&quot;&amp;"));
        
        // Edge case
        assertEquals("", Entities.unescape(""));
        
        // Error scenario
        assertNull(Entities.unescape(null));
    }
}