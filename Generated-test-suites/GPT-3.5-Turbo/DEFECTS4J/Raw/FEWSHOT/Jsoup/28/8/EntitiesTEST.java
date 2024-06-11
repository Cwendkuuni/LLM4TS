import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.jsoup.nodes.Entities;
import org.junit.Test;

public class EntitiesTest {
    // Test isNamedEntity() method
    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("foo"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
    }

    // Test getCharacterByName() method
    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName("foo"));
        assertEquals('&', Entities.getCharacterByName("amp").charValue());
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
        assertEquals('>', Entities.getCharacterByName("gt").charValue());
        assertEquals('\'', Entities.getCharacterByName("apos").charValue());
        assertEquals('"', Entities.getCharacterByName("quot").charValue());
    }

    // Test escape() method
    @Test
    public void testEscape() {
        assertEquals("", Entities.escape("", null));
        assertEquals("lt", Entities.escape("<", null));
        assertEquals("gt", Entities.escape(">", null));
        assertEquals("amp", Entities.escape("&", null));
        assertEquals("apos", Entities.escape("'", null));
        assertEquals("quot", Entities.escape("\"", null));
        
        assertEquals("f", Entities.escape("f", null));
        assertEquals("foo", Entities.escape("foo", null));
        assertEquals("&", Entities.escape("&", null));
        assertEquals("&#10;", Entities.escape("\n", null));
        assertEquals("&#x3C;", Entities.escape("\u003C", null));
    }

    // Test unescape() method
    @Test
    public void testUnescape() {
        assertEquals("", Entities.unescape(""));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        
        assertEquals("&amp", Entities.unescape("&amp"));
        assertEquals("foo", Entities.unescape("foo"));
        assertEquals("\n", Entities.unescape("&#10;"));
        assertEquals("\u003C", Entities.unescape("&#x3C;"));
    }
    
    // Test escape() method with EscapeMode.xhtml
    @Test
    public void testEscapeWithXhtmlMode() {
        assertEquals("&lt;", Entities.escape("<", Entities.EscapeMode.xhtml));
        assertEquals("&gt;", Entities.escape(">", Entities.EscapeMode.xhtml));
        assertEquals("&amp;", Entities.escape("&", Entities.EscapeMode.xhtml));
        assertEquals("&apos;", Entities.escape("'", Entities.EscapeMode.xhtml));
        assertEquals("&quot;", Entities.escape("\"", Entities.EscapeMode.xhtml));
        
        assertEquals("f", Entities.escape("f", Entities.EscapeMode.xhtml));
        assertEquals("foo", Entities.escape("foo", Entities.EscapeMode.xhtml));
        assertEquals("&", Entities.escape("&", Entities.EscapeMode.xhtml));
        assertEquals("\n", Entities.escape("\n", Entities.EscapeMode.xhtml));
        assertEquals("&#x3C;", Entities.escape("\u003C", Entities.EscapeMode.xhtml));
    }
    
    // Test escape() method with EscapeMode.base
    @Test
    public void testEscapeWithBaseMode() {
        assertEquals("", Entities.escape("", Entities.EscapeMode.base));
        assertEquals("lt", Entities.escape("<", Entities.EscapeMode.base));
        assertEquals("gt", Entities.escape(">", Entities.EscapeMode.base));
        assertEquals("amp", Entities.escape("&", Entities.EscapeMode.base));
        assertEquals("apos", Entities.escape("'", Entities.EscapeMode.base));
        assertEquals("quot", Entities.escape("\"", Entities.EscapeMode.base));
        
        assertEquals("f", Entities.escape("f", Entities.EscapeMode.base));
        assertEquals("foo", Entities.escape("foo", Entities.EscapeMode.base));
        assertEquals("&", Entities.escape("&", Entities.EscapeMode.base));
        assertEquals("&#10;", Entities.escape("\n", Entities.EscapeMode.base));
        assertEquals("\u003C", Entities.escape("\u003C", Entities.EscapeMode.base));
    }
    
    // Test escape() method with EscapeMode.extended
    @Test
    public void testEscapeWithExtendedMode() {
        assertEquals("", Entities.escape("", Entities.EscapeMode.extended));
        assertEquals("lt", Entities.escape("<", Entities.EscapeMode.extended));
        assertEquals("gt", Entities.escape(">", Entities.EscapeMode.extended));
        assertEquals("amp", Entities.escape("&", Entities.EscapeMode.extended));
        assertEquals("apos", Entities.escape("'", Entities.EscapeMode.extended));
        assertEquals("quot", Entities.escape("\"", Entities.EscapeMode.extended));
        
        assertEquals("f", Entities.escape("f", Entities.EscapeMode.extended));
        assertEquals("foo", Entities.escape("foo", Entities.EscapeMode.extended));
        assertEquals("&", Entities.escape("&", Entities.EscapeMode.extended));
        assertEquals("\n", Entities.escape("\n", Entities.EscapeMode.extended));
        assertEquals("\u003C", Entities.escape("\u003C", Entities.EscapeMode.extended));
    }
    
    // Test loadEntities() method
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        
        assertEquals(5, entities.size());
        assertTrue(entities.containsKey("lt"));
        assertTrue(entities.containsKey("gt"));
        assertTrue(entities.containsKey("amp"));
        assertTrue(entities.containsKey("apos"));
        assertTrue(entities.containsKey("quot"));
    }
    
    // Test toCharacterKey() method
    @Test
    public void testToCharacterKey() {
        Map<Character, String> inMap = Entities.baseByVal;
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        
        assertEquals(inMap.size(), outMap.size());
        assertTrue(outMap.containsKey('&'));
        assertTrue(outMap.containsKey('<'));
        assertTrue(outMap.containsKey('>'));
        assertTrue(outMap.containsKey('\''));
        assertTrue(outMap.containsKey('"'));
    }
}