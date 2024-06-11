import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;&#65;", Entities.escape("<>&'\"A", null, Entities.EscapeMode.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&'\"A", Entities.unescape("&lt;&gt;&amp;&apos;&quot;&#65;"));
    }
}