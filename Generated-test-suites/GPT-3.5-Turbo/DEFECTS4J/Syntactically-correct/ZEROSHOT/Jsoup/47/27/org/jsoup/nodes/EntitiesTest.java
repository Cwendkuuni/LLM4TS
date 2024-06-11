import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;div&gt;", Entities.escape("<div>", new Document.OutputSettings()));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
        assertEquals("&#xa0;", Entities.escape("\u00a0", new Document.OutputSettings(EscapeMode.xhtml)));
    }

    @Test
    public void testUnescape() {
        assertEquals("<div>", Entities.unescape("&lt;div&gt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00a0", Entities.unescape("&#xa0;"));
        assertEquals("&", Entities.unescape("&#38;"));
    }

}