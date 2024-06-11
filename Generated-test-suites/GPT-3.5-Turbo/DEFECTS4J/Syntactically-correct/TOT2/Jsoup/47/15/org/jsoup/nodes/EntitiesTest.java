import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
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
        assertEquals("&lt;&amp;", Entities.escape("<&", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>", Entities.unescape("&lt;&gt;"));
    }

    @Test
    public void testEdgeCases() {
        // Test edge case of empty string
        assertEquals("", Entities.escape("", new Document.OutputSettings()));
        
        // Test edge case of all whitespace
        assertEquals("   ", Entities.escape("   ", new Document.OutputSettings()));
        
        // Test edge case of long string
        assertEquals("&lt;&amp;&gt;", Entities.escape("<<&>>", new Document.OutputSettings()));

        // Test edge case of unescaping an empty string
        assertEquals("", Entities.unescape(""));
        
        // Test edge case of unescaping a string without entities
        assertEquals("abc", Entities.unescape("abc"));
        
        // Test edge case of unescaping a string with entities
        assertEquals("<>\"&", Entities.unescape("&lt;&gt;&quot;&amp;"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEscapeWithIOException() {
        Entities.escape("test", null);
    }

}