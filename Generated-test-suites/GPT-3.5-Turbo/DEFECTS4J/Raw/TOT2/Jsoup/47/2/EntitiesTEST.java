import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("nbsp"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        // Test typical use case
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\""));

        // Test edge cases
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        // Test typical use case
        assertEquals("<>&\"'", Entities.unescape("&lt;&gt;&amp;&quot;'"));

        // Test edge cases
        assertEquals("", Entities.unescape("", false));
        assertEquals("", Entities.unescape("", true));
    }

    // Additional tests for other methods can be added here

}