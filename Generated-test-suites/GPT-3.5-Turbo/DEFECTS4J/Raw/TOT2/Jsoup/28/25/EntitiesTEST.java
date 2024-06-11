import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        Character c = Entities.getCharacterByName("amp");
        assertEquals(Character.valueOf('&'), c);
    }

    @Test
    public void testEscape() {
        EscapeMode mode = EscapeMode.xhtml;
        String escaped = Entities.escape("Hello < & >", null, mode);
        assertEquals("Hello &lt; &amp; &gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt; &amp; &gt;");
        assertEquals("< & >", unescaped);
    }

    // Add more test cases for other methods if needed

}