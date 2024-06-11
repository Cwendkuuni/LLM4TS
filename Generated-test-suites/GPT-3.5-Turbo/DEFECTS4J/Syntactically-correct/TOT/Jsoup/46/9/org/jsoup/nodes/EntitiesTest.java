
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    // Test for isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // expected true
        assertFalse(Entities.isNamedEntity("abc")); // expected false
    }

    // Test for isBaseNamedEntity() method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // expected true
        assertFalse(Entities.isBaseNamedEntity("abc")); // expected false
    }

    // Test for getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // expected '<'
        assertNull(Entities.getCharacterByName("abc")); // expected null
    }

    // Test for escape() method
    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&", null)); // expected "&amp;"
        assertEquals("&quot;", Entities.escape("\"", null)); // expected "&quot;"
        assertEquals("&lt;", Entities.escape("<", null)); // expected "&lt;"
        assertEquals("&gt;", Entities.escape(">", null)); // expected "&gt;"
        assertEquals("&nbsp;", Entities.escape(" ", null)); // expected "&nbsp;"
        
        // Testing normaliseWhite parameter
        assertEquals("a b", Entities.escape("a\tb", null, false, true, false)); // expected "a b"
        
        // Testing inAttribute parameter
        assertEquals("&lt;", Entities.escape("<", null, true, false, false)); // expected "&lt;"
        assertEquals("<", Entities.escape("<", null, false, false, false)); // expected "<"
    }

    // Test for unescape() method
    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;")); // expected "&"
        assertEquals("\"", Entities.unescape("&quot;")); // expected "\""
        assertEquals("<", Entities.unescape("&lt;")); // expected "<"
        assertEquals(">", Entities.unescape("&gt;")); // expected ">"
        
        // Testing strict parameter
        assertEquals("\"", Entities.unescape("&quot")); // expected "\""
        assertEquals(";", Entities.unescape("&quot;;")); // expected ";"
    }
}
