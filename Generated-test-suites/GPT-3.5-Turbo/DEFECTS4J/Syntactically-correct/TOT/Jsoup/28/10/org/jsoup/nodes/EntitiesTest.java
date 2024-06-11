import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
    
        // Typical use case test
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge case test
        assertFalse(Entities.isNamedEntity("abcd"));
        
        // Error scenario test
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
    
        // Typical use case test
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Edge case test
        assertNull(Entities.getCharacterByName("abcd"));
        
        // Error scenario test
        assertNull(Entities.getCharacterByName(""));
    }
    
    @Test
    public void testEscape() {
    
        // Typical use case test
        String escapedString = Entities.escape("<html>", Document.OutputSettings.xhtml);
        assertEquals("&lt;html&gt;", escapedString);
        
        // Edge case test
        escapedString = Entities.escape("", Document.OutputSettings.xhtml);
        assertEquals("", escapedString);
        
        // Error scenario test
        escapedString = Entities.escape("<html>", null);
        assertEquals("<html>", escapedString);
    }
    
    @Test
    public void testUnescape() {
    
        // Typical use case test
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
        
        // Edge case test
        unescapedString = Entities.unescape("");
        assertEquals("", unescapedString);
        
        // Error scenario test
        unescapedString = Entities.unescape("&lt;html");
        assertEquals("&lt;html", unescapedString);
    }
    
}