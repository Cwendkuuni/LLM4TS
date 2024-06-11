package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("abc"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(null, Entities.getCharacterByName("abc"));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        
        String escapeResult = Entities.escape("<html>", out);
        assertEquals("&lt;html&gt;", escapeResult);
    }
    
    @Test
    public void testUnescape() {
        String unescapeResult = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapeResult);
    }
}