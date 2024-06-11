import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<html>", null, EscapeMode.xhtml);
        assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        assertEquals("<html>", unescapedString);
    }

    // Additional test cases for edge cases and error scenarios can be added here

}