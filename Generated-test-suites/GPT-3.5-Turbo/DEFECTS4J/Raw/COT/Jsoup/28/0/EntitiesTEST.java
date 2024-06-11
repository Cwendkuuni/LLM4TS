
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
        assertEquals("&lt;hello&gt;", Entities.escape("<hello>", Document.OutputSettings.xhtml()));
        assertEquals("&lt;hello&gt;", Entities.escape("<hello>", Document.OutputSettings.html()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml()));
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.html()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.xhtml()));
        assertEquals("&quot;", Entities.escape("\"", Document.OutputSettings.html()));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.xhtml()));
        assertEquals("&apos;", Entities.escape("'", Document.OutputSettings.html()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.xhtml()));
        assertEquals("&gt;", Entities.escape(">", Document.OutputSettings.html()));
    }

    @Test
    public void testUnescape() {
        assertEquals("<hello>", Entities.unescape("&lt;hello&gt;"));
        assertEquals("<hello>", Entities.unescape("&lt;hello&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals(">", Entities.unescape("&gt;"));
    }
}

