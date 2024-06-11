import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
 
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
    }
  
    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
    }
  
    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        assertNotNull(lt);
        assertEquals('<', lt.charValue());
      
        Character gt = Entities.getCharacterByName("gt");
        assertNotNull(gt);
        assertEquals('>', gt.charValue());
      
        Character amp = Entities.getCharacterByName("amp");
        assertNotNull(amp);
        assertEquals('&', amp.charValue());
      
        Character quot = Entities.getCharacterByName("quot");
        assertNotNull(quot);
        assertEquals('"', quot.charValue());
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