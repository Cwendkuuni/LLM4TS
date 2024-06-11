package org.jsoup.nodes;
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
        String escapedString = Entities.escape("<strong>hello</strong>", new Document.OutputSettings());
        assertEquals("&lt;strong&gt;hello&lt;/strong&gt;", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;strong&gt;hello&lt;/strong&gt;");
        assertEquals("<strong>hello</strong>", unescapedString);
    }
}