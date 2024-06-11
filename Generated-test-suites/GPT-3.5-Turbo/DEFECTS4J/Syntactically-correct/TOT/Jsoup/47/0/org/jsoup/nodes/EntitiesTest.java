
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: empty string
        assertFalse(Entities.isNamedEntity(""));

        // Error scenario: non-existent entity
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("amp"));

        // Edge case: empty string
        assertFalse(Entities.isBaseNamedEntity(""));

        // Error scenario: non-existent entity
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case: empty string
        assertNull(Entities.getCharacterByName(""));

        // Error scenario: non-existent entity
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Typical use case
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\""));

        // Edge case: empty string
        assertEquals("", Entities.escape(""));

        // Edge case: string with no escape characters
        assertEquals("Hello world", Entities.escape("Hello world"));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;"));

        // Edge case: empty string
        assertEquals("", Entities.unescape(""));

        // Error scenario: string with invalid escape sequence (missing ';')
        assertEquals("&lt;&gt;&amp;&quot;", Entities.unescape("&lt&gt&amp&quot"));
    }
}
