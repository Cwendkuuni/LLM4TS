import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    // Test isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    // Test getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    // Test escape method
    @Test
    public void testEscape() {
        String escaped = Entities.escape("<>&", Document.OutputSettings.html());
        assertEquals("&lt;&gt;&amp;", escaped);
    }
    
    // Test unescape method
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;");
        assertEquals("<>&", unescaped);
    }
    
    // Test unescape method with strict parameter
    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;", true);
        assertEquals("<>&", unescaped);
    }
    
    // Test edge case - unescape method with invalid input
    @Test
    public void testUnescapeInvalidInput() {
        String unescaped = Entities.unescape("&invalid;");
        assertEquals("&invalid;", unescaped);
    }
    
    // Add more test cases for other methods if needed
    
}