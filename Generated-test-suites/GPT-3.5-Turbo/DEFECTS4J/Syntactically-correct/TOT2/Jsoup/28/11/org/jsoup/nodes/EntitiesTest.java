import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape() {
        String input = "abc";
        assertEquals("abc", Entities.escape(input, new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        assertEquals("abc", Entities.escape(input, new Document.OutputSettings(), Entities.EscapeMode.base));
        assertEquals("abc", Entities.escape(input, new Document.OutputSettings(), Entities.EscapeMode.extended));
    }
    
    @Test
    public void testUnescape() {
        String input = "&amp;";
        assertEquals("&", Entities.unescape(input));
    }
    
    // Add more test methods for other public methods in the class
    
}