
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("invalid");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);
        
        result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue(result);
        
        result = Entities.isBaseNamedEntity("gt");
        Assert.assertTrue(result);
        
        result = Entities.isBaseNamedEntity("quot");
        Assert.assertTrue(result);
        
        result = Entities.isBaseNamedEntity("invalid");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals(new Character('<'), result);
        
        result = Entities.getCharacterByName("amp");
        Assert.assertEquals(new Character('&'), result);
        
        result = Entities.getCharacterByName("gt");
        Assert.assertEquals(new Character('>'), result);
        
        result = Entities.getCharacterByName("quot");
        Assert.assertEquals(new Character('"'), result);
        
        result = Entities.getCharacterByName("invalid");
        Assert.assertNull(result);
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("Hello & World", Document.OutputSettings.base());
        Assert.assertEquals("Hello &amp; World", result);
    }
    
    @Test
    public void testEscape_withInvalidCharacter() {
        String result = Entities.escape("\uD83D\uDE01", Document.OutputSettings.base());
        Assert.assertEquals("&#x1f601;", result);
    }
    
    @Test
    public void testUnescape() {
        String result = Entities.unescape("Hello &amp; World");
        Assert.assertEquals("Hello & World", result);
    }
    
    @Test
    public void testUnescape_withInvalidEntity() {
        String result = Entities.unescape("Hello &invalid;");
        Assert.assertEquals("Hello &invalid;", result);
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> result = Entities.loadEntities("entities-base.properties");
        Assert.assertEquals(2527, result.size());
    }
    
    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Map<Character, String> result = Entities.toCharacterKey(entities);
        Assert.assertEquals(2527, result.size());
    }
}
