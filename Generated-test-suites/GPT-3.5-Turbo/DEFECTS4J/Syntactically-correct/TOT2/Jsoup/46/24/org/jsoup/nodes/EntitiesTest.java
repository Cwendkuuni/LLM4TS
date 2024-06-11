import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Entities.EscapeMode mode = Entities.EscapeMode.xhtml;
        String result = Entities.escape("Hello & there <>", new Document.OutputSettings().escapeMode(mode));
        assertEquals("Hello &amp; there &lt;&gt;", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("Hello &amp; there &lt;&gt;");
        assertEquals("Hello & there <>", result);
    }
    
    // Add more test cases for additional methods and edge cases as needed

}