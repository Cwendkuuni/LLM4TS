
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }
    
    @Test
    public void testEscape() {
        String escaped1 = Entities.escape("This is < a test >", Entities.EscapeMode.base);
        assertEquals("This is &lt; a test &gt;", escaped1);
        
        String escaped2 = Entities.escape("This is < a test >", Entities.EscapeMode.xhtml);
        assertEquals("This is &lt; a test &gt;", escaped2);
        
        String escaped3 = Entities.escape("This is < a test >", Entities.EscapeMode.extended);
        assertEquals("This is &lt; a test &gt;", escaped3);
    }
    
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This is &lt; a test &gt;");
        assertEquals("This is < a test >", unescaped);
    }
    
    // Edge cases and Exception handling scenarios
    
    @Test
    public void testIsNamedEntityWithNullInput() {
        assertFalse(Entities.isNamedEntity(null));
    }
    
    @Test
    public void testGetCharacterByNameWithNullInput() {
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscapeWithNullString() {
        String escaped = Entities.escape(null, Entities.EscapeMode.base);
        assertNull(escaped);
    }
    
    @Test
    public void testEscapeWithNullEscapeMode() {
        String escaped = Entities.escape("This is < a test >", null);
        assertEquals("This is < a test >", escaped);
    }
    
    @Test
    public void testUnescapeWithNullString() {
        String unescaped = Entities.unescape(null);
        assertNull(unescaped);
    }
    
    @Test
    public void testUnescapeWithEmptyString() {
        String unescaped = Entities.unescape("");
        assertEquals("", unescaped);
    }
    
    @Test
    public void testUnescapeWithStrictTrue() {
        String unescaped = Entities.unescape("This is &lt a test &gt;", true);
        assertEquals("This is &lt a test &gt;", unescaped);
    }
    
    @Test
    public void testUnescapeWithInvalidEntity() {
        String unescaped = Entities.unescape("This is &fakeentity;");
        assertEquals("This is &fakeentity;", unescaped);
    }
    
    @Test
    public void testLoadEntitiesWithFailedResourceLoad() {
        try {
            HashMap<String, Character> entities = new HashMap<>();
            Entities.loadEntities("fakeFileName.properties");
            fail("Should have thrown an exception");
        } catch (MissingResourceException e) {
            assertEquals("Error loading entities resource: null", e.getMessage());
        }
    }
    
    @Test
    public void testToCharacterKeyWithDuplicateNames() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("name1", 'a');
        inMap.put("name2", 'a');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        
        assertEquals("name1", outMap.get('a'));
        assertFalse(outMap.containsKey('a'));
        assertNull(outMap.get('b'));
    }
}

