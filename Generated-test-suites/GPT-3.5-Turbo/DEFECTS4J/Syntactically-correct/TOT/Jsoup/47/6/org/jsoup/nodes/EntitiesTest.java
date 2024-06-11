
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // Test cases for isNamedEntity() method
    @Test
    public void testIsNamedEntityValid() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntityInvalid() {
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    // Test cases for isBaseNamedEntity() method
    @Test
    public void testIsBaseNamedEntityValid() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testIsBaseNamedEntityInvalid() {
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    // Test cases for getCharacterByName() method
    @Test
    public void testGetCharacterByNameValid() {
        Character character = Entities.getCharacterByName("lt");
        assertEquals(Character.valueOf('<'), character);
    }

    @Test
    public void testGetCharacterByNameInvalid() {
        Character character = Entities.getCharacterByName("invalid");
        assertNull(character);
    }

    // Test cases for escape() method
    @Test
    public void testEscape() {
        // Typical use case
        String escapedString = Entities.escape("<script>alert('Hello World!');</script>", null);
        assertEquals("&lt;script&gt;alert('Hello World!');&lt;/script&gt;", escapedString);

        // Edge case when empty string is passed
        escapedString = Entities.escape("", null);
        assertEquals("", escapedString);
    }

    // Test cases for unescape() method
    @Test
    public void testUnescape() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;script&gt;alert('Hello World!');&lt;/script&gt;");
        assertEquals("<script>alert('Hello World!');</script>", unescapedString);

        // Edge case when empty string is passed
        unescapedString = Entities.unescape("");
        assertEquals("", unescapedString);
    }

}
