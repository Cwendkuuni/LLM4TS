
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // edge case: empty string
        assertFalse(Entities.isNamedEntity(""));
        
        // error scenario: invalid entity name
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));
        
        // edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));
        
        // error scenario: invalid entity name
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // typical use case
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        
        // edge case: empty string
        assertNull(Entities.getCharacterByName(""));
        
        // error scenario: invalid entity name
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape() {
        // typical use case
        String escaped = Entities.escape("This is <b>bold</b> text");
        assertEquals("This is &lt;b&gt;bold&lt;/b&gt; text", escaped);
        
        // edge case: empty string
        escaped = Entities.escape("");
        assertEquals("", escaped);
    }
    
    @Test
    public void testUnescape() {
        // typical use case
        String unescaped = Entities.unescape("This is &lt;b&gt;bold&lt;/b&gt; text");
        assertEquals("This is <b>bold</b> text", unescaped);

        // edge case: empty string
        unescaped = Entities.unescape("");
        assertEquals("", unescaped);

        // error scenario: strict unescaping
        unescaped = Entities.unescape("This is &amp;amp;lt;b&amp;amp;gt;bold&amp;amp;lt;/b&amp;amp;gt; text", true);
        assertEquals("This is &amp;lt;b&amp;gt;bold&amp;lt;/b&amp;gt; text", unescaped);
    }
}

