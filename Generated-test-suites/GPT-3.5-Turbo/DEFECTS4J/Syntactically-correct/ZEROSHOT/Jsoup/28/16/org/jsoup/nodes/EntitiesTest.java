import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntityWithValidName() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsNamedEntityWithInvalidName() {
        boolean result = Entities.isNamedEntity("xyz");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("amp");
        Assert.assertEquals('&', character.charValue());
    }
    
    @Test
    public void testEscape() {
        String string = "Hello <world>";
        String escapedString = Entities.escape(string, null);
        Assert.assertEquals("Hello &lt;world&gt;", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String string = "Hello &lt;world&gt;";
        String unescapedString = Entities.unescape(string);
        Assert.assertEquals("Hello <world>", unescapedString);
    }
    
    @Test
    public void testEscapeWithEncoderAndEscapeMode() {
        String string = "Hello <world>";
        String escapedString = Entities.escape(string, null, Entities.EscapeMode.xhtml);
        Assert.assertEquals("Hello &lt;world&gt;", escapedString);
    }
    
    @Test
    public void testUnescapeWithStrictFalse() {
        String string = "Hello &lt;world&gt;";
        String unescapedString = Entities.unescape(string, false);
        Assert.assertEquals("Hello <world>", unescapedString);
    }
    
    @Test
    public void testUnescapeWithStrictTrue() {
        String string = "Hello &lt;world&gt;";
        String unescapedString = Entities.unescape(string, true);
        Assert.assertEquals("Hello <world>", unescapedString);
    }
}