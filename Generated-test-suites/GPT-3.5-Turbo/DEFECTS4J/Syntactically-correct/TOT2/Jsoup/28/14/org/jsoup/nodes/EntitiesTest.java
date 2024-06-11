import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        assertEquals("This & that", Entities.escape("This & that", EscapeMode.base.getMap()));
    }

    @Test
    public void testUnescape() {
        assertEquals("This & that", Entities.unescape("This &amp; that"));
    }

    // Add additional test cases for edge cases and error scenarios here

}