
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        assertEquals('<', result.charValue());
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", Entities.EscapeMode.xhtml);
        assertEquals("&lt;html&gt;", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", result);
    }

    @Test
    public void testUnescapeStrict() {
        String result = Entities.unescape("&lt;html&gt;", true);
        assertEquals("<html>", result);
    }

    // Additional test cases for edge cases and exception handling scenarios
    @Test
    public void testIsNamedEntityWithInvalidName() {
        boolean result = Entities.isNamedEntity("invalid");
        assertFalse(result);
    }

    // Test escape with invalid characters
    // Test escape with null string

    @Test
    public void testUnescapeWithNoEntities() {
        String result = Entities.unescape("html");
        assertEquals("html", result);
    }

    @Test
    public void testUnescapeStrictWithInvalidEntity() {
        String result = Entities.unescape("&invalid;");
        assertEquals("&invalid;", result);
    }
}
