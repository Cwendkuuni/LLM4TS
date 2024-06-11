
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        
        assertFalse(Entities.isNamedEntity("abc"));
        assertFalse(Entities.isNamedEntity(""));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        
        assertFalse(Entities.isBaseNamedEntity("abc"));
        assertFalse(Entities.isBaseNamedEntity(""));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        
        assertNull(Entities.getCharacterByName("abc"));
        assertNull(Entities.getCharacterByName(""));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.xhtml()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml()));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        
        assertEquals("&lt", Entities.unescape("&lt"));
        assertEquals("&abc;", Entities.unescape("&abc;"));
    }
}
