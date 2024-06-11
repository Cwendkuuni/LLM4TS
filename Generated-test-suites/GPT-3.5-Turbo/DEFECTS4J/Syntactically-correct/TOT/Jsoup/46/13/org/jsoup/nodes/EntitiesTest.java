import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: Empty String
        assertFalse(Entities.isNamedEntity(""));

        // Error scenario: Unknown entity name
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));

        // Edge case: Empty String
        assertFalse(Entities.isBaseNamedEntity(""));

        // Error scenario: Unknown entity name
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case: Empty String
        assertNull(Entities.getCharacterByName(""));

        // Error scenario: Unknown entity name
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        // Typical use case: escape '<' character
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(Entities.EscapeMode.xhtml)));

        // Edge case: Empty string
        assertEquals("", Entities.escape("", new Document.OutputSettings(Entities.EscapeMode.xhtml)));

        // Error scenario: Null string
        assertThrows(NullPointerException.class, () -> {
            Entities.escape(null, new Document.OutputSettings(Entities.EscapeMode.xhtml));
        });
    }

    @Test
    public void testUnescape() {
        // Typical use case: unescape '&lt;' string
        assertEquals("<", Entities.unescape("&lt;"));

        // Edge case: Empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: Null string
        assertThrows(NullPointerException.class, () -> {
            Entities.unescape(null);
        });
    }

    @Test
    public void testUnescapeStrict() {
        // Typical use case: unescape '&lt;' string
        assertEquals("<", Entities.unescape("&lt;", true));

        // Edge case: Empty string
        assertEquals("", Entities.unescape("", true));

        // Error scenario: Null string
        assertThrows(NullPointerException.class, () -> {
            Entities.unescape(null, true);
        });
    }
}