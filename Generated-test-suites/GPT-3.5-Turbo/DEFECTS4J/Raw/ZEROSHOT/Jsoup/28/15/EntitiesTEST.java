import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('\"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);

        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&gt;", Entities.escape(">", out));
        assertEquals("&apos;", Entities.escape("'", out));
        assertEquals("&quot;", Entities.escape("\"", out));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("&#x1F600;", Entities.unescape("&#x1F600;"));
    }
}