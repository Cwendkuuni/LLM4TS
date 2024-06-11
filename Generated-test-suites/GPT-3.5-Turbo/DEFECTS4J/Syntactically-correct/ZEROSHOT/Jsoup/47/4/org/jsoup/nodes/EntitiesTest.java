
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 60), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 62), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf((char) 38), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf((char) 34), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }
    
    @Test
    public void testEscape() {
        assertEquals("&amp;lt;&gt;&quot;", Entities.escape("&<>\""));
        assertEquals("&lt;test&gt;", Entities.escape("<test>", Document.OutputSettings.xhtml()));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("&<>\"", Entities.unescape("&amp;&lt;&gt;&quot;"));
        assertEquals("<test>", Entities.unescape("&lt;test&gt;"));
    }

    @Test
    public void testEscapeMode() {
        Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
        assertEquals("&", xhtmlMap.get((char) 38));
        assertEquals("<", xhtmlMap.get((char) 60));
        assertEquals(">", xhtmlMap.get((char) 62));
        assertEquals("\"", xhtmlMap.get((char) 34));
        
        Map<Character, String> baseMap = Entities.EscapeMode.base.getMap();
        assertEquals("&", baseMap.get((char) 38));
        assertEquals("<", baseMap.get((char) 60));
        assertEquals(">", baseMap.get((char) 62));
        assertNull(baseMap.get((char) 34));
        
        Map<Character, String> extendedMap = Entities.EscapeMode.extended.getMap();
        assertEquals("&", extendedMap.get((char) 38));
        assertEquals("<", extendedMap.get((char) 60));
        assertEquals(">", extendedMap.get((char) 62));
        assertEquals("\"", extendedMap.get((char) 34));
    }
}
