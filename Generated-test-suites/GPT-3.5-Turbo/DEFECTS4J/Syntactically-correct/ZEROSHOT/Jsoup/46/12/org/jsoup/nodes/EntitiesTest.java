import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test cases for known named entities
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        // Test cases for unknown named entities
        assertFalse(Entities.isNamedEntity("xyz"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test cases for known named entities in base set
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        // Test cases for known named entities not in base set
        assertFalse(Entities.isBaseNamedEntity("nbsp"));
        assertFalse(Entities.isBaseNamedEntity("apos"));

        // Test cases for unknown named entities
        assertFalse(Entities.isBaseNamedEntity("xyz"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test cases for known named entities
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));

        // Test cases for unknown named entities
        assertNull(Entities.getCharacterByName("xyz"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        // Test cases for escaping special characters
        assertEquals("&lt;", Entities.escape("<", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.html()));
        assertEquals("&amp;amp;", Entities.escape("&amp;", Document.OutputSettings.html()));

        // Test cases for escaping non-ASCII characters
        assertEquals("&#x20ac;", Entities.escape("€", Document.OutputSettings.html()));
        assertEquals("&#x10348;", Entities.escape("𐍈", Document.OutputSettings.html()));
    }

    @Test
    public void testUnescape() {
        // Test cases for unescaping special characters
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&amp;", Entities.unescape("&amp;amp;"));

        // Test cases for unescaping numeric character references
        assertEquals("€", Entities.unescape("&#8364;"));
        assertEquals("𐍈", Entities.unescape("&#66312;"));
    }

}