import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("test"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("test"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf((char) 0x00022), Entities.getCharacterByName("quot"));
    }

    @Test
    public void testEscape() {
        String input = "<>&quot;test";
        String expected = "&lt;&gt;&amp;quot;test";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testEscapeEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;&gt;&amp;quot;test";
        String expected = "<>&quot;test";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescapeEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, Entities.unescape(input));
    }

}