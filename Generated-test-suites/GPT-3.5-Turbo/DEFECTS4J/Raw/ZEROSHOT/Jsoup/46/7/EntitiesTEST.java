import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        assertEquals("abc", Entities.escape("abc", new Document.OutputSettings()));
    }

    @Test
    public void testEscape_withEntities() {
        assertEquals("&lt;abc&gt;", Entities.escape("<abc>", new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        assertEquals("abc", Entities.unescape("abc"));
    }

    @Test
    public void testUnescape_withEntities() {
        assertEquals("<abc>", Entities.unescape("&lt;abc&gt;"));
    }
}