import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml));
        assertEquals("amp", Entities.escape("amp", Document.OutputSettings.xhtml));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("amp", Entities.unescape("amp"));
    }
    
    @Test
    public void testEscapeWithEncoderAndEscapeMode() {
        assertEquals("&lt;", Entities.escape("<", null, Entities.EscapeMode.xhtml));
        assertEquals("amp", Entities.escape("amp", null, Entities.EscapeMode.xhtml));
    }
    
    @Test
    public void testUnescapeWithStrict() {
        assertEquals("&lt;", Entities.unescape("&lt;", true));
        assertEquals("&lt;", Entities.unescape("&lt;", false));
    }
}