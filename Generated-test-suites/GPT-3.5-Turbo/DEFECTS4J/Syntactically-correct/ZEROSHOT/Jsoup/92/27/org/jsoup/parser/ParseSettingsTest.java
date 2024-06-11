import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertTrue(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String tagName = "DIV";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String attributeName = "CLASS";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("src", "example.png");
        attributes.put("ALT", "Example Image");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("src", normalizedAttributes.keySet().toArray()[0]);
        assertEquals("alt", normalizedAttributes.keySet().toArray()[1]);
    }
}