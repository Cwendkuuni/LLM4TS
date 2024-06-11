
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("test"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("test"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&lt; &amp;", Entities.escape("< &", new Document.OutputSettings()));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("< &", Entities.unescape("&lt; &amp;"));
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}