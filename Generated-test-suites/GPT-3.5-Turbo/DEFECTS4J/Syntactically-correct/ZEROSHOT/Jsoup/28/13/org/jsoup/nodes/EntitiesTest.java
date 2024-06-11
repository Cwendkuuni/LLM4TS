import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        assertEquals("&amp;", Entities.escape("&", Document.OutputSettings.xhtml));
        assertEquals("abc&amp;def", Entities.escape("abc&def", Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("abc&def", Entities.unescape("abc&amp;def"));
    }
}