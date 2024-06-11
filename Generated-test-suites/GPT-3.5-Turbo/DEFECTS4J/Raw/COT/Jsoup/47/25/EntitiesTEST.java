
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", null));
        assertEquals("&amp;", Entities.escape("&", null));
        assertEquals("abc", Entities.escape("abc", null));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("abc", Entities.unescape("abc"));
    }
    
    // Additional test cases to cover edge cases and exceptions

    @Test
    public void testEscapeWithNullString() {
        assertNull(Entities.escape(null, null));
    }

    @Test
    public void testEscapeWithEmptyString() {
        assertEquals("", Entities.escape("", null));
    }

    @Test
    public void testEscapeWithAttribute() {
        assertEquals("&#x26;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    }

    @Test
    public void testUnescapeWithStrict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals("&amp;", Entities.unescape("&amp;", true));
        assertEquals("abc", Entities.unescape("abc", true));
        assertEquals("&lt;", Entities.unescape("&lt;", false));
        assertEquals("&amp;", Entities.unescape("&amp;", false));
        assertEquals("abc", Entities.unescape("abc", false));
    }

    @Test
    public void testUnescapeWithMissingSemicolon() {
        assertEquals("&lt", Entities.unescape("&lt"));
        assertEquals("&amp", Entities.unescape("&amp"));
        assertEquals("abc", Entities.unescape("abc"));
    }
}
