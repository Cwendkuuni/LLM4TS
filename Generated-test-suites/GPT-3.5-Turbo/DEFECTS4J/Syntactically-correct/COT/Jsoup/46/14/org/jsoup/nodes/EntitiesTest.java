
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foo"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foo"));
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("Hello < World", Document.OutputSettings.html());
        assertEquals("Hello &lt; World", result);
    }
    
    @Test
    public void testUnescape() {
        String result = Entities.unescape("Hello &lt; World");
        assertEquals("Hello < World", result);
    }
    
    // Additional Tests

    @Test
    public void testEscapeInAttribute() {
        String result = Entities.escape("\"Hello\"", Document.OutputSettings.html());
        assertEquals("&quot;Hello&quot;", result);
    }
    
    @Test
    public void testEscapeExtendedCharacters() {
        String result = Entities.escape("Čtest", Document.OutputSettings.html());
        assertEquals("Čtest", result);
    }
    
    @Test
    public void testUnescapeStrict() {
        String result = Entities.unescape("&lt;", true);
        assertEquals("<", result);
    }
    
    @Test
    public void testUnescapeNonStrict() {
        String result = Entities.unescape("&lt");
        assertEquals("<", result);
    }
}

