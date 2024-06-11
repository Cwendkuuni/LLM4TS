import org.junit.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case
        assertFalse(Entities.isNamedEntity("invalid"));

        // Error scenario
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Typical use case
        assertTrue(Entities.isBaseNamedEntity("lt"));

        // Edge case
        assertFalse(Entities.isBaseNamedEntity("invalid"));

        // Error scenario
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case
        assertNull(Entities.getCharacterByName("invalid"));

        // Error scenario
        assertNull(Entities.getCharacterByName(null));
    }
    
    @Test
    public void testEscape() {
        // Typical use case
        String escapedString = Entities.escape("<script>alert('XSS');</script>", Document.OutputSettings.prettyPrint());
        assertEquals("&lt;script&gt;alert('XSS');&lt;/script&gt;", escapedString);

        // Edge case
        String emptyString = Entities.escape("", Document.OutputSettings.prettyPrint());
        assertEquals("", emptyString);

        // Error scenario
        assertNull(Entities.escape(null, Document.OutputSettings.prettyPrint()));
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String unescapedString = Entities.unescape("&lt;script&gt;alert('XSS');&lt;/script&gt;");
        assertEquals("<script>alert('XSS');</script>", unescapedString);

        // Edge case
        String emptyString = Entities.unescape("");
        assertEquals("", emptyString);

        // Error scenario
        assertNull(Entities.unescape(null));
    }

    // Additional test cases for other methods...

}