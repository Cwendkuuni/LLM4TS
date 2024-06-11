import org.jsoup.nodes.Entities;
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
        // Test edge case for empty string
        assertEquals("", Entities.escape("", new Document.OutputSettings()));

        // Test typical case
        assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\"", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        // Test typical case
        assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;"));

        // Test case with strict
        assertEquals("<>&", Entities.unescape("&lt;&gt&&quot;", true));
    }
}