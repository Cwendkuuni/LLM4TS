import org.jsoup.nodes.Entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // edge case: empty string
        assertFalse(Entities.isNamedEntity(""));
        
        // edge case: non-existent entity
        assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // typical use case
        Character character = Entities.getCharacterByName("lt");
        assertEquals('<', character.charValue());
        
        // edge case: non-existent entity
        assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        // typical use case
        String escapedString = Entities.escape("This is <b>bold</b> text.");
        assertEquals("This is &lt;b&gt;bold&lt;/b&gt; text.", escapedString);
        
        // edge case: empty string
        String escapedEmpty = Entities.escape("");
        assertEquals("", escapedEmpty);
        
        // error scenario: null input
        String escapedNull = Entities.escape(null);
        assertNull(escapedNull);
    }
    
    @Test
    public void testUnescape() {
        // typical use case: unescape HTML
        String unescapedString = Entities.unescape("This is &lt;b&gt;bold&lt;/b&gt; text.");
        assertEquals("This is <b>bold</b> text.", unescapedString);
        
        // edge case: empty string
        String unescapedEmpty = Entities.unescape("");
        assertEquals("", unescapedEmpty);
        
        // error scenario: null input
        String unescapedNull = Entities.unescape(null);
        assertNull(unescapedNull);
    }

}