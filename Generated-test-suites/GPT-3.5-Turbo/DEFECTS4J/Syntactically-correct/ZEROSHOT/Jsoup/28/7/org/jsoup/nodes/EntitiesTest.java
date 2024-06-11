import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", null));
        assertEquals("&amp;", Entities.escape("&", null));
        assertEquals("&apos;", Entities.escape("'", null));
        assertEquals("&gt;", Entities.escape(">", null));
        assertEquals("&quot;", Entities.escape("\"", null));
        assertEquals("&#8364;", Entities.escape("€", null));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("'", Entities.unescape("&apos;"));
        assertEquals(">", Entities.unescape("&gt;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("€", Entities.unescape("&#8364;"));
    }

}