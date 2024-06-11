package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge case
        assertFalse(Entities.isNamedEntity("nonexistententity"));
        
        // Error scenario
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));
        
        // Edge case
        assertFalse(Entities.isBaseNamedEntity("nonexistententity"));
        
        // Error scenario
        assertFalse(Entities.isBaseNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Typical use case
        Character character = Entities.getCharacterByName("lt");
        assertNotNull(character);
        assertEquals('<', character.charValue());
        
        // Edge case
        character = Entities.getCharacterByName("nonexistententity");
        assertNull(character);
        
        // Error scenario
        character = Entities.getCharacterByName(null);
        assertNull(character);
    }
    
    @Test
    public void testEscape() {
        // Typical use case
        String escapedString = Entities.escape("<html>", new Document.OutputSettings());
        assertEquals("&lt;html&gt;", escapedString);
        
        // Edge case
        escapedString = Entities.escape("", new Document.OutputSettings());
        assertEquals("", escapedString);
        
        // Error scenario
        escapedString = Entities.escape(null, new Document.OutputSettings());
        assertNull(escapedString);
    }
    
    @Test
    public void testUnescape() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
        
        // Edge case
        unescapedString = Entities.unescape("");
        assertEquals("", unescapedString);
        
        // Error scenario
        unescapedString = Entities.unescape(null);
        assertNull(unescapedString);
    }
}