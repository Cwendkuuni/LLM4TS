import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Edge cases
        assertFalse(Entities.isNamedEntity("foo")); // Entity doesn't exist
        assertFalse(Entities.isNamedEntity("amp;")); // Invalid entity format
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("amp"));
        
        // Edge cases
        assertFalse(Entities.isBaseNamedEntity("lt")); // Only present in extended entities
        assertFalse(Entities.isBaseNamedEntity("foo")); // Entity doesn't exist
    }
    
    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Edge cases
        assertNull(Entities.getCharacterByName("foo")); // Entity doesn't exist
    }
    
    @Test
    public void testEscape() {
        // Typical use case
        String escaped = Entities.escape("&lt;html&gt;", new Document.OutputSettings());
        assertEquals("&amp;lt;html&amp;gt;", escaped);
        
        // Edge cases
        String emptyString = Entities.escape("", new Document.OutputSettings());
        assertEquals("", emptyString); // Empty string should remain the same
    }
    
    @Test
    public void testUnescape() {
        // Typical use case
        String unescaped = Entities.unescape("&amp;lt;html&amp;gt;");
        assertEquals("&lt;html&gt;", unescaped);
        
        // Edge cases
        String emptyString = Entities.unescape("");
        assertEquals("", emptyString); // Empty string should remain the same
    }
}