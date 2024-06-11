import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape() {
        String escaped = Entities.escape("<html>");
        assertEquals("&lt;html&gt;", escaped);
    }
    
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescaped);
    }
}