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
    public void testGetCharacterByName() {
       assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
       assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscape() {
       String escaped = Entities.escape("test string", null, EscapeMode.xhtml);
       assertEquals("test string", escaped);
    }

    @Test
    public void testUnescape() {
       String unescaped = Entities.unescape("&lt;&gt;&amp;");
       assertEquals("<>&", unescaped);
    }

    // Add more test cases here based on identified edge cases and exceptions

}