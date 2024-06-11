
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((Character) '<', Entities.getCharacterByName("lt"));
        assertEquals((Character) '>', Entities.getCharacterByName("gt"));
        assertEquals((Character) '&', Entities.getCharacterByName("amp"));
        assertEquals((Character) '"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;", Entities.escape("<", null));
        assertEquals("&gt;", Entities.escape(">", null));
        assertEquals("&amp;", Entities.escape("&", null));
        assertEquals("&quot;", Entities.escape("\"", null));
        assertEquals(" ", Entities.escape("\u00A0", null));
        assertEquals("&#x65;", Entities.escape("e", null));
    }

    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;", false));
        assertEquals(">", Entities.unescape("&gt;", false));
        assertEquals("&", Entities.unescape("&amp;", false));
        assertEquals("\"", Entities.unescape("&quot;", false));
        assertEquals("\u00A0", Entities.unescape("&#160;", false));
        assertEquals("e", Entities.unescape("&#x65;", false));
    }
}
