package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("gt"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("gt"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x0003C), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf((char) 0x00026), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("gt"));
    }
    
    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<html>", new Document.OutputSettings());
        assertEquals("&lt;html&gt;", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
    }
    
    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("&lt;html&gt;", true);
        assertEquals("&lt;html&gt;", unescapedString);
    }
}