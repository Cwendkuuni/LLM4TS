package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<html>", new Document.OutputSettings());
        assertEquals("&lt;html&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescaped);
    }

    // Add more test cases for other methods as needed

}