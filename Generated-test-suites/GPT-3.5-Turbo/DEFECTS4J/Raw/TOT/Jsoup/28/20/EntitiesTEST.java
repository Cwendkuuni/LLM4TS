import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Typical use case
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge case: unknown entity name
        assertFalse(Entities.isNamedEntity("foo"));

        // Error scenario: empty input
        assertFalse(Entities.isNamedEntity(""));
    }

    @Test
    public void testGetCharacterByName() {
        // Typical use case
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));

        // Edge case: unknown entity name
        assertNull(Entities.getCharacterByName("foo"));

        // Error scenario: empty input
        assertNull(Entities.getCharacterByName(""));
    }

    @Test
    public void testEscape() {
        // Typical use case
        String escaped = Entities.escape("This is an <example> string", Document.OutputSettings.prettyPrint());
        assertEquals("This is an &lt;example&gt; string", escaped);

        // Edge case: empty string
        escaped = Entities.escape("", Document.OutputSettings.prettyPrint());
        assertEquals("", escaped);

        // Error scenario: null input
        escaped = Entities.escape(null, Document.OutputSettings.prettyPrint());
        assertNull(escaped);
    }

    @Test
    public void testUnescape() {
        // Typical use case
        String unescaped = Entities.unescape("This is an &lt;example&gt; string");
        assertEquals("This is an <example> string", unescaped);

        // Edge case: empty string
        unescaped = Entities.unescape("");
        assertEquals("", unescaped);

        // Error scenario: null input
        unescaped = Entities.unescape(null);
        assertNull(unescaped);
    }
}