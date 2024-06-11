import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // Test a known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        
        // Test an unknown named entity
        assertFalse(Entities.isNamedEntity("unknown"));
    }
    
    @Test
    public void testGetCharacterByName() {
        // Test a known named entity
        assertEquals('&', (char) Entities.getCharacterByName("amp"));
        
        // Test an unknown named entity
        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        // Test escaping special characters
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        
        // Test a string that can be encoded without escaping
        assertEquals("abcd1234", Entities.escape("abcd1234", Document.OutputSettings.html()));
        
        // Test a string that cannot be encoded and should be replaced with a numeric character reference
        assertEquals("&#65533;", Entities.escape("�", Document.OutputSettings.html()));
    }
    
    @Test
    public void testUnescape() {
        // Test unescaping special characters
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        
        // Test unescaping a string without any special characters
        assertEquals("abcd1234", Entities.unescape("abcd1234"));
        
        // Test unescaping a string with a numeric character reference
        assertEquals("�", Entities.unescape("&#65533;"));
    }
}