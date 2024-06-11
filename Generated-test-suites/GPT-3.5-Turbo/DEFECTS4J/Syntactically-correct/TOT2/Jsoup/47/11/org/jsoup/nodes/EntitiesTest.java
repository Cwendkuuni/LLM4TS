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
        // Test typical use case
        assertEquals("&lt;&gt;&amp;", Entities.escape("<>&", null));
        // Test edge case - empty string
        assertEquals("", Entities.escape("", null));
        // Test error scenario - null input
        assertNull(Entities.escape(null, null));
    }

    @Test
    public void testUnescape() {
        // Test typical use case
        assertEquals("<>&", Entities.unescape("&lt;&gt;&amp;"));
        // Test edge case - empty string
        assertEquals("", Entities.unescape(""));
        // Test error scenario - null input
        assertNull(Entities.unescape(null));
    }

    // Add more test cases for other methods if needed
    
}