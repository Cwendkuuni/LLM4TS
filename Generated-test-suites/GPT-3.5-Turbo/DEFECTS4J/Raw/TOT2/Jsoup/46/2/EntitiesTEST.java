import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("dummy"));
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("dummy"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
        assertEquals("&amp;&lt;&#x000A0;&#x22;&#x3C;&#x3E;&#x22;", Entities.escape("&<\u00A0\"<>\"", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("", Entities.unescape(""));
        assertEquals("&<\u00A0\"<>\"", Entities.unescape("&amp;&lt;&#x000A0;&#x22;&#x3C;&#x3E;&#x22", false));
    }

    // Edge case when trying to get character by invalid name
    @Test
    public void testGetCharacterByNameInvalidName() {
        assertNull(Entities.getCharacterByName("invalid"));
    }

    // Exception handling when trying to unescape invalid string with strict mode
    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeStrictModeException() {
        Entities.unescape("invalid", true);
    }

}