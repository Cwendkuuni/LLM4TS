import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.nio.charset.StandardCharsets;
import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("unknown"));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscapeWithXhtmlEncoder() {
        String escaped = Entities.escape("&", Document.OutputSettings.xhtml);
        assertEquals("&amp;", escaped);
    }

    @Test
    public void testEscapeWithCustomEncoderAndMode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        String escaped = Entities.escape("©", encoder, Entities.EscapeMode.xhtml);
        assertEquals("&#169;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;&#169;");
        assertEquals("<©", unescaped);
    }

    @Test
    public void testUnescapeWithStrictMode() {
        String unescaped = Entities.unescape("&lt&#169;", true);
        assertEquals("<&#169;", unescaped);
    }

    @Test
    public void testLoadEntities() {
        assertEquals(Character.valueOf('<'), Entities.loadEntities("entities-base.properties").get("lt"));
        assertEquals(Character.valueOf('"'), Entities.loadEntities("entities-base.properties").get("quot"));
    }

    @Test
    public void testToCharacterKey() {
        assertEquals("lt", Entities.toCharacterKey(Entities.loadEntities("entities-base.properties")).get('<'));
        assertNull(Entities.toCharacterKey(null));
    }
}