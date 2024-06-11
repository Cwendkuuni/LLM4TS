import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        assertEquals("", Entities.escape("", null));
    }

    @Test
    public void testUnescape() {
        assertEquals(">>", Entities.unescape("&gt;&gt;"));
    }
}