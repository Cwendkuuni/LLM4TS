package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("notexist"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("notexist"));
    }
    
    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        assertEquals(character, Character.valueOf('<'));
        assertNull(Entities.getCharacterByName("notexist"));
    }
    
    @Test
    public void testEscape() {
        String escaped = Entities.escape("Hello &lt;World&gt;");
        assertEquals("Hello &amp;lt;World&amp;gt;", escaped);
    }
    
    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("Hello &amp;lt;World&amp;gt;");
        assertEquals("Hello <World>", unescaped);
    }
    
    @Test
    public void testEscapeModeGetMap() {
        Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
        assertNotNull(map);
        assertEquals(4, map.size());
    }
}