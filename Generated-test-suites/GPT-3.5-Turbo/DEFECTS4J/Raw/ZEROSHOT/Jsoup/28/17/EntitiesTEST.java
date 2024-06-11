import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("notAnEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("notAnEntity"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("\"Hello & World<>\"", Document.OutputSettings.xhtml);
        assertEquals("&quot;Hello &amp; World&lt;&gt;&quot;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&quot;Hello &amp; World&lt;&gt;&quot;");
        assertEquals("\"Hello & World<>\"", unescapedString);
    }
}