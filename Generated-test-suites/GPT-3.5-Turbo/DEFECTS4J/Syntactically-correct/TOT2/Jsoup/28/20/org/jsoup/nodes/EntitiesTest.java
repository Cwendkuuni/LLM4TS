import org.jsoup.nodes.Entities.EscapeMode;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        EscapeMode escapeMode = EscapeMode.xhtml;
        String escaped = Entities.escape("<>&\"", null, escapeMode);
        assertEquals("&lt;&gt;&amp;&quot;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;&quot;");
        assertEquals("<>&\"", unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;&quot;", true);
        assertEquals("<>&\"", unescaped);
    }

    @Test
    public void testLoadEntities() {
        assertNotNull(Entities.loadEntities("entities-base.properties"));
        assertNotNull(Entities.loadEntities("entities-full.properties"));
    }

    @Test
    public void testToCharacterKey() {
        assertNotNull(Entities.toCharacterKey(Entities.loadEntities("entities-base.properties")));
    }

}