import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("amp");
        assertEquals('&', result.charValue());
        Character result2 = Entities.getCharacterByName("fakeEntity");
        assertNull(result2);
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        String result = Entities.escape("<div>", encoder, Entities.EscapeMode.xhtml);
        assertEquals("&lt;div&gt;", result);
    }

    @Test
    public void testUnescape() {
        String original = "&lt;div&gt;";
        String result = Entities.unescape(original);
        assertEquals("<div>", result);
    }

    @Test
    public void testUnescapeWithInvalidInput() {
        String original = "&invalid;";
        String result = Entities.unescape(original);
        assertEquals("&invalid;", result);
    }

    @Test
    public void testLoadEntities() {
        Entities entities = new Entities();
        assertNotNull(entities.loadEntities("entities-base.properties"));
    }

    @Test
    public void testToCharacterKey() {
        Entities entities = new Entities();
        assertNotNull(entities.toCharacterKey(Entities.full));
    }
}