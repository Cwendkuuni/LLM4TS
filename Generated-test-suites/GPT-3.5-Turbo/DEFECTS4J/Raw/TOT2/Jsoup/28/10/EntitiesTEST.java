import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String escapedString = Entities.escape("Hello & World", CharsetEncoder.UTF_8, Entities.EscapeMode.xhtml);
        assertEquals("Hello &amp; World", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &amp; World");
        assertEquals("Hello & World", unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("Hello &amp; World", true);
        assertEquals("Hello &amp; World", unescapedString);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities);
        assertEquals(5, entities.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inputMap = new HashMap<>();
        inputMap.put("name1", 'A');
        inputMap.put("name2", 'B');

        Map<Character, String> outputMap = Entities.toCharacterKey(inputMap);
        assertNotNull(outputMap);
        assertEquals("name2", outputMap.get('B'));
    }
}