
import org.jsoup.nodes.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("xyz")); // Non-existent entity name
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("xyz")); // Non-existent entity name
    }

    @Test
    public void testEscape() {
        assertEquals("Hello &lt; world", Entities.escape("Hello < world", Document.OutputSettings.xhtml));
        assertEquals("Hello &amp; world", Entities.escape("Hello & world", Document.OutputSettings.xhtml));
        assertEquals("Hello &apos; world", Entities.escape("Hello ' world", Document.OutputSettings.xhtml));
        assertEquals("Hello &gt; world", Entities.escape("Hello > world", Document.OutputSettings.xhtml));
        assertEquals("Hello &quot; world", Entities.escape("Hello \" world", Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("Hello < world", Entities.unescape("Hello &lt; world"));
        assertEquals("Hello & world", Entities.unescape("Hello &amp; world"));
        assertEquals("Hello ' world", Entities.unescape("Hello &apos; world"));
        assertEquals("Hello > world", Entities.unescape("Hello &gt; world"));
        assertEquals("Hello \" world", Entities.unescape("Hello &quot; world"));
        assertEquals("Hello &xyz;", Entities.unescape("Hello &xyz;")); // Non-existent entity name
    }
}
