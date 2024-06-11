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
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Test escaping special characters
        assertEquals("&amp;&lt;&gt;&quot;", Entities.escape("&<>\"", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        // Test unescaping HTML entities with strict flag false
        assertEquals("abc&def", Entities.unescape("abc&amp;def"));
    }

    @Test
    public void testUnescapeStrict() {
        // Test unescaping HTML entities with strict flag true
        assertEquals("abc&def;", Entities.unescape("abc&amp;def;"));
    }

    // Add more test cases for edge cases and exception handling scenarios as needed

}