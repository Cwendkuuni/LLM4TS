import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities.EscapeMode;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        // Test case 1: Known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test case 2: Unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        // Test case 1: Known named entity in base set
        assertTrue(Entities.isBaseNamedEntity("lt"));
        
        // Test case 2: Unknown named entity in base set
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Test case 1: Known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        
        // Test case 2: Unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        // Test case 1: Normal input
        assertEquals("&amp;lt;", Entities.escape("&lt;", new Document.OutputSettings(EscapeMode.xhtml)));
        
        // Test case 2: Empty string input
        assertEquals("", Entities.escape("", new Document.OutputSettings(EscapeMode.xhtml)));
    }
    
    @Test
    public void testUnescape() {
        // Test case 1: Normal input
        assertEquals("&lt;", Entities.unescape("&amp;lt;"));
        
        // Test case 2: Empty string input
        assertEquals("", Entities.unescape(""));
    }
}