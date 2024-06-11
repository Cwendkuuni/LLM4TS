import org.jsoup.nodes.Entities;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Character entityCharacter = Entities.getCharacterByName("gt");
        assertEquals('>', entityCharacter.charValue());
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("This is a test string", null);
        assertEquals("This is a test string", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("This is a test string");
        assertEquals("This is a test string", result);
    }

    @Test
    public void testUnescapeStrict() {
        String result = Entities.unescape("This is a test string", true);
        assertEquals("This is a test string", result);
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testEscapeWithSpecialCharacters() {
        String result = Entities.escape("<hello>", null);
        assertEquals("&lt;hello&gt;", result);
    }
    
    @Test
    public void testEscapeWithAmpersand() {
        String result = Entities.escape("Test & Test", null);
        assertEquals("Test &amp; Test", result);
    }

    @Test
    public void testEscapeWithInvalidCharacters() {
        String result = Entities.escape("ÄÖÜ", null);
        assertEquals("ÄÖÜ", result);
    }

    @Test
    public void testUnescapeInvalidString() {
        String result = Entities.unescape("&invalid;");
        assertEquals("&invalid;", result);
    }
    
    // Add more test cases for edge cases and exceptions if necessary
    
    // 
    
    // TODO: Add more test cases as needed
    
    // 
}