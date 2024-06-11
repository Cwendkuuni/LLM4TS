import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: Empty string
        assertFalse(Entities.isNamedEntity(""));

        // Edge case: Non-existent entity
        assertFalse(Entities.isNamedEntity("nonexistent"));

        // Error scenario: Null input
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case: Empty string
        assertNull(Entities.getCharacterByName(""));

        // Edge case: Non-existent entity
        assertNull(Entities.getCharacterByName("nonexistent"));

        // Error scenario: Null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        // Typical use case: HTML input
        String input = "<html>";
        assertEquals("&lt;html&gt;", Entities.escape(input, Entities.EscapeMode.xhtml));

        // Typical use case: Ampersand
        input = "5 > 4 & 3 < 2";
        assertEquals("5 &gt; 4 &amp; 3 &lt; 2", Entities.escape(input, Entities.EscapeMode.xhtml));

        // Edge case: Empty string
        input = "";
        assertEquals("", Entities.escape(input, Entities.EscapeMode.xhtml));

        // Error scenario: Null input
        assertNull(Entities.escape(null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        // Typical use case: HTML input
        String input = "&lt;html&gt;";
        assertEquals("<html>", Entities.unescape(input));

        // Typical use case: Ampersand
        input = "5 &gt; 4 &amp; 3 &lt; 2";
        assertEquals("5 > 4 & 3 < 2", Entities.unescape(input));

        // Edge case: Empty string
        input = "";
        assertEquals("", Entities.unescape(input));

        // Error scenario: Null input
        assertNull(Entities.unescape(null));
    }

}