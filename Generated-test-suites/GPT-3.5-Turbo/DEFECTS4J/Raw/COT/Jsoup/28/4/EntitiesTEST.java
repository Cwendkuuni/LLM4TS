
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String input = "<div>&copy;</div>";
        String expected = "&lt;div&gt;&amp;copy;&lt;/div&gt;";
        assertEquals(expected, Entities.escape(input, Document.OutputSettings.quiet()));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;&amp;copy;&lt;/div&gt;";
        String expected = "<div>&copy;</div>";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescape_Strict() {
        String input = "&lt;div&amp;copy;&lt;/div>";
        String expected = "<div&copy;</div>";
        assertEquals(expected, Entities.unescape(input, true));
    }
}

