import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("gt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("quot"));
        Assert.assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("gt"));
        Assert.assertNull(Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("Hello & World", Entity.EscapeMode.xhtml);
        Assert.assertEquals("Hello &amp; World", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &amp; World");
        Assert.assertEquals("Hello & World", unescapedString);
    }
  
    @Test
    public void testEscapeWithOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entity.EscapeMode.base);
        String escapedString = Entities.escape("Hello & World", outputSettings);
        Assert.assertEquals("Hello &amp; World", escapedString);
    }
    
    @Test
    public void testEscapeWithAttributes() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entity.EscapeMode.xhtml);
        String escapedString = Entities.escape("Hello & World", outputSettings, true, true, true);
        Assert.assertEquals("Hello &amp; World", escapedString);
    }

    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("Hello &amp; World", true);
        Assert.assertEquals("Hello & World", unescapedString);
    }

    @Test
    public void testUnescapeNonStrict() {
        String unescapedString = Entities.unescape("Hello &amp; World", false);
        Assert.assertEquals("Hello & World", unescapedString);
    }

    @Test
    public void testCanEncode() {
        boolean canEncode = Entities.canEncode(Entities.CoreCharset.ascii, '<', CharsetEncoder.defaultCharset());
        Assert.assertTrue(canEncode);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Assert.assertEquals(25, entities.size());
    }

    @Test
    public void testToCharacterKey() {
        Map<Character, String> characterKeyMap = Entities.toCharacterKey(Entities.base);
        Assert.assertEquals(25, characterKeyMap.size());
    }
}