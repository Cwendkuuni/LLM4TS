package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {
    @Before
    public void setUp() {
        // any necessary setup for the test cases
    }
    
    @Test
    public void testIsNamedEntity_validName_returnsTrue() {
        assertTrue(Entities.isNamedEntity("lt"));
    }
    
    @Test
    public void testIsNamedEntity_invalidName_returnsFalse() {
        assertFalse(Entities.isNamedEntity("abc"));
    }
    
    @Test
    public void testGetCharacterByName_validName_returnsCharacter() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }
    
    @Test
    public void testGetCharacterByName_invalidName_returnsNull() {
        assertNull(Entities.getCharacterByName("abc"));
    }
    
    @Test
    public void testEscape_validString_returnsEscapedString() {
        String escapedString = Entities.escape("<html>", null, Entities.EscapeMode.xhtml);
        assertEquals("&lt;html&gt;", escapedString);
    }
    
    @Test
    public void testEscape_invalidCharsetEncoder_returnsUnchangedString() {
        String str = "\u00A9";
        String escapedString = Entities.escape(str, null, Entities.EscapeMode.xhtml);
        assertEquals(str, escapedString);
    }
    
    @Test
    public void testEscape_invalidEscapeMode_returnsUnchangedString() {
        String str = "<html>";
        String escapedString = Entities.escape(str, null, null);
        assertEquals(str, escapedString);
    }
    
    @Test
    public void testUnescape_validString_returnsUnescapedString() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
    }
    
    @Test
    public void testUnescape_stringWithoutEntities_returnsUnchangedString() {
        String str = "Hello World";
        String unescapedString = Entities.unescape(str);
        assertEquals(str, unescapedString);
    }
    
    @Test
    public void testUnescape_invalidString_returnsOriginalString() {
        String str = "&abc;";
        String unescapedString = Entities.unescape(str);
        assertEquals(str, unescapedString);
    }
}
