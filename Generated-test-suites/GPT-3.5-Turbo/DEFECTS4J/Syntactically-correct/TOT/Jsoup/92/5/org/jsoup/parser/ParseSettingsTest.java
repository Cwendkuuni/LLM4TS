
import org.junit.Test;
import org.junit.Assert;

import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        boolean result = settings.preserveTagCase();
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        
        boolean result = settings.preserveAttributeCase();
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "aBcDeF";
        
        String result = settings.normalizeTag(tagName);
        
        Assert.assertEquals("abcdef", result);
    }
    
    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "aBcDeF";
        
        String result = settings.normalizeTag(tagName);
        
        Assert.assertEquals("aBcDeF", result);
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "attRIBUTE";
        
        String result = settings.normalizeAttribute(attributeName);
        
        Assert.assertEquals("attribute", result);
    }
    
    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "attRIBUTE";
        
        String result = settings.normalizeAttribute(attributeName);
        
        Assert.assertEquals("attRIBUTE", result);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        
        Attributes result = settings.normalizeAttributes(attributes);
        
        Assert.assertEquals("value1", result.get("attr1"));
    }
    
    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        
        Attributes result = settings.normalizeAttributes(attributes);
        
        Assert.assertEquals("value1", result.get("attr1"));
    }
}
