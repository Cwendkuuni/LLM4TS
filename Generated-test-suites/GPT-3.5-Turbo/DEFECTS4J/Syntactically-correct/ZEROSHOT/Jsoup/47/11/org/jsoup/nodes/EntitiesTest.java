import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Assert;
import org.junit.Test;
import java.util.Map;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("abc"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("gt");
        Assert.assertEquals('>', character.charValue());
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", new Document.OutputSettings());
        Assert.assertEquals("&lt;html&gt;", result);
    }
    
    @Test
    public void testEscapeWithAttribute() {
        String result = Entities.escape("<img src=\"image.png\">", new Document.OutputSettings());
        Assert.assertEquals("&lt;img src=\"image.png\"&gt;", result);
    }
    
    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        Assert.assertEquals("<html>", result);
    }
    
    @Test
    public void testUnescapeWithStrict() {
        String result = Entities.unescape("&lt;html&gt;", true);
        Assert.assertEquals("<html>", result);
    }
    
    @Test
    public void testUnescapeWithoutStrict() {
        String result = Entities.unescape("&lt;html&gt;", false);
        Assert.assertEquals("<html>", result);
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Assert.assertFalse(entities.isEmpty());
        Assert.assertTrue(entities.containsKey("lt"));
        Assert.assertEquals(Character.valueOf('<'), entities.get("lt"));
    }
    
    @Test
    public void testToCharacterKey() {
        Map<Character, String> map = Entities.toCharacterKey(Entities.loadEntities("entities-base.properties"));
        Assert.assertFalse(map.isEmpty());
        Assert.assertTrue(map.containsKey('<'));
        Assert.assertEquals("lt", map.get('<'));
    }
    
}