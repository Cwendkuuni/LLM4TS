
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertFalse(Entities.isNamedEntity("abc")); // unknown entity
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("xyz")); // unknown entity in base set
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("gt"); // typical use case
        assertNotNull(character);
        assertEquals('>', character.charValue());

        character = Entities.getCharacterByName("abc"); // unknown entity
        assertNull(character);
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<p>Hello, World!</p>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
        assertEquals("&lt;p&gt;Hello, World!&lt;/p&gt;", escapedString); // typical use case

        escapedString = Entities.escape("<p>Hello, World!</p>", new Document.OutputSettings());
        assertEquals("&lt;p&gt;Hello, World!&lt;/p&gt;", escapedString); // default escape mode
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;p&gt;Hello, World!&lt;/p&gt;");
        assertEquals("<p>Hello, World!</p>", unescapedString); // typical use case

        unescapedString = Entities.unescape("abc");
        assertEquals("abc", unescapedString); // no escape characters
    }
}
