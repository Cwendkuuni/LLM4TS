import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }
    
    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('&', Entities.getCharacterByName("amp").charValue());
        Assert.assertEquals('<', Entities.getCharacterByName("lt").charValue());
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }
    
    @Test
    public void testEscape() {
        Assert.assertEquals("&lt;", Entities.escape("<", null));
        Assert.assertEquals("&amp;", Entities.escape("&", null));
        Assert.assertEquals("abc", Entities.escape("abc", null));
    }
    
    @Test
    public void testUnescape() {
        Assert.assertEquals("<", Entities.unescape("&lt;"));
        Assert.assertEquals("&", Entities.unescape("&amp;"));
        Assert.assertEquals("abc", Entities.unescape("abc"));
    }
    
    @Test
    public void testUnescapeStrict() {
        Assert.assertEquals("<", Entities.unescape("&lt;", true));
        Assert.assertEquals("&", Entities.unescape("&amp;", true));
        Assert.assertEquals("abc", Entities.unescape("abc", true));
    }
    
    @Test
    public void testUnescapeInvalidEntity() {
        Assert.assertEquals("&xyz;", Entities.unescape("&xyz;"));
        Assert.assertEquals("&abc;", Entities.unescape("&abc;"));
    }
    
    @Test
    public void testUnescapeHexEncoding() {
        Assert.assertEquals("A", Entities.unescape("&#41;", false));
        Assert.assertEquals("A", Entities.unescape("&#x41;", false));
    }
    
}