import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals((Character) '<', Entities.getCharacterByName("lt"));
        Assert.assertEquals((Character) '&', Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<html>", Document.OutputSettings.base());
        Assert.assertEquals("&lt;html&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;html&gt;");
        Assert.assertEquals("<html>", unescapedString);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Assert.assertTrue(entities.containsKey("lt"));
        Assert.assertTrue(entities.containsKey("amp"));
        Assert.assertFalse(entities.containsKey("invalid"));
    }
    
    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesInvalidResource() {
        Entities.loadEntities("invalid.properties");
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<String, Character>();
        inMap.put("lt", '<');
        inMap.put("amp", '&');
        
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        Assert.assertEquals("lt", outMap.get('<'));
        Assert.assertEquals("amp", outMap.get('&'));
    }
}