import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ParseSettingsTest {
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "HREF";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", normalizedAttributeName);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("src", "image.png");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals("src", normalizedAttributes.asList().get(0).getKey());
        assertEquals("image.png", normalizedAttributes.asList().get(0).getValue());
    }
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        boolean preserveTagCase = parseSettings.preserveTagCase();
        assertEquals(true, preserveTagCase);
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        boolean preserveAttributeCase = parseSettings.preserveAttributeCase();
        assertEquals(true, preserveAttributeCase);
    }
    
    @Test
    public void testConstructor() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        boolean preserveTagCase = parseSettings.preserveTagCase();
        boolean preserveAttributeCase = parseSettings.preserveAttributeCase();
        
        assertEquals(true, preserveTagCase);
        assertEquals(true, preserveAttributeCase);
    }
}