import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals((Character)'<', character);
    }

    @Test
    public void testEscape() {
        String string = "&lt;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String result = Entities.escape(string, outputSettings);
        Assert.assertEquals("&amp;lt;", result);
    }

    @Test
    public void testUnescape() {
        String string = "&amp;lt;";
        String result = Entities.unescape(string);
        Assert.assertEquals("<", result);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&amp;lt;";
        String result = Entities.unescape(string, true);
        Assert.assertEquals("<", result);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = Entities.CoreCharset.ascii;
        char c = '<';
        boolean result = Entities.canEncode(charset, c, null);
        Assert.assertTrue(result);
    }

    @Test
    public void testLoadEntities() {
        String filename = "entities-full.properties";
        Map<String, Character> entities = Entities.loadEntities(filename);
        Assert.assertNotNull(entities);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-full.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        Assert.assertNotNull(outMap);
    }
}