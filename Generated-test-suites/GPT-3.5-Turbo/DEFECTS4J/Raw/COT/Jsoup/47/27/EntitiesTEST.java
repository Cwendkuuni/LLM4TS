import org.jsoup.nodes.Entities;
import org.junit.Test;
import org.junit.Assert;
import java.util.Map;

public class EntitiesTest {

    // Test isNamedEntity()
    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    // Test isBaseNamedEntity()
    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("invalid"));
    }
    
    // Test getCharacterByName()
    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('\0'), Entities.getCharacterByName("invalid"));
    }
    
    // Test escape()
    @Test
    public void testEscape() {
        // Test normal case
        String escapedString = Entities.escape("& < > \"", new Document.OutputSettings(Entities.EscapeMode.base));
        Assert.assertEquals("&amp; &lt; &gt; &quot;", escapedString);
        
        // Test edge case
        escapedString = Entities.escape("&#x0391;&#x0392;", new Document.OutputSettings(Entities.EscapeMode.xhtml));
        Assert.assertEquals("ΑΒ", escapedString);
    }

    // Test unescape()
    @Test
    public void testUnescape() {
        // Test normal case
        String unescapedString = Entities.unescape("&amp; &lt; &gt; &quot;");
        Assert.assertEquals("& < > \"", unescapedString);
        
        // Test edge case
        unescapedString = Entities.unescape("&#x0391;&#x0392;");
        Assert.assertEquals("ΑΒ", unescapedString);
    }
}