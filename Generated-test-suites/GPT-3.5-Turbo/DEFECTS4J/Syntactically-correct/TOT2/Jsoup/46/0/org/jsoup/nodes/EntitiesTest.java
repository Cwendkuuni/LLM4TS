import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // Add test cases for escape method
    }

    @Test
    public void testUnescape() {
        // Add test cases for unescape method
    }

    @Test
    public void testEscapeSpecialCharacters() {
        EscapeMode xhtml = EscapeMode.xhtml;
        assertEquals("&", Entities.escape("&", xhtml));
        assertEquals("&#x000e9;", Entities.escape("é", xhtml));
    }

    @Test
    public void testUnescapeString() {
        assertEquals("&eacute;", Entities.unescape("&eacute;"));
    }

    @Test
    public void testLoadEntities() {
        // Add test cases for loadEntities method
    }

    @Test
    public void testToCharacterKey() {
        // Add test cases for toCharacterKey method
    }
}