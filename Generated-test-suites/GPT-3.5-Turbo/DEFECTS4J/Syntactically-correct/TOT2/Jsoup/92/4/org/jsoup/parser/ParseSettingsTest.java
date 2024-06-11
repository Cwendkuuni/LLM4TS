import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "HREF";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "#");
        
        attributes = parseSettings.normalizeAttributes(attributes);
        
        assertNotNull(attributes.get("href"));
    }
}