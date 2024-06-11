import org.junit.Test;
import static org.junit.Assert.*;

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
        String escaped = Entities.escape("Hello <World>", Document.OutputSettings.html());
        assertEquals("Hello &lt;World&gt;", escaped);
    }
    
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("Hello &lt;World&gt;");
        assertEquals("Hello <World>", unescaped);
    }
    
    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("&ltWorld&gt", true);
        assertEquals("<World>", unescaped);
    }
  
    // Add additional test cases for other methods as needed
  
}