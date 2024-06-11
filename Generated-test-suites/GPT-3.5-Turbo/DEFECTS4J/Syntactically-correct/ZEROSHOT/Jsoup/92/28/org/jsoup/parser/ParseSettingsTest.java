import org.junit.Assert;
import org.junit.Test;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Assert.assertTrue(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Assert.assertTrue(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        Assert.assertEquals("div", parseSettings.normalizeTag(tagName));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "HREF";
        Assert.assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        Assert.assertEquals("href", normalizedAttributes.getKey(0));
        Assert.assertEquals("http://example.com", normalizedAttributes.getValue("href"));
    }
}