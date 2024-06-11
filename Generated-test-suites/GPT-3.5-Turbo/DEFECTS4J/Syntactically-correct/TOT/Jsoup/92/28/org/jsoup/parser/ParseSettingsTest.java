import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Assert.assertTrue(settings.preserveTagCase());
        
        String tagName = "DIV";
        String normalizedTag = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTag);
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Assert.assertTrue(settings.preserveAttributeCase());
        
        String attributeName = "SRC";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttribute);
    }
    
    @Test
    public void testNormalizeTagWithPreservedTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        String tagName = "\nDIV  \t";
        String normalizedTag = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName.trim(), normalizedTag);
    }
    
    @Test
    public void testNormalizeTagWithoutPreservedTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        
        String tagName = "\nDIV  \t";
        String normalizedTag = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName.trim().toLowerCase(), normalizedTag);
    }
    
    @Test
    public void testNormalizeAttributeWithPreservedAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        String attributeName = "\nSRC  \t";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName.trim(), normalizedAttribute);
    }
    
    @Test
    public void testNormalizeAttributeWithoutPreservedAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        
        String attributeName = "\nSRC  \t";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName.trim().toLowerCase(), normalizedAttribute);
    }
    
    @Test
    public void testNormalizeAttributesWithPreservedAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("SRC", "image.png");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        Assert.assertEquals(attributes, normalizedAttributes);
    }
    
    @Test
    public void testNormalizeAttributesWithoutPreservedAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("SRC", "image.png");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        Assert.assertNotEquals(attributes, normalizedAttributes);
        Assert.assertTrue(normalizedAttributes.containsKey("href"));
        Assert.assertTrue(normalizedAttributes.containsKey("src"));
    }
}