import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static junit.framework.TestCase.assertEquals;
import static org.jsoup.internal.Normalizer.lowerCase;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        String tagName = "Tag";
        String normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("tag", normalizedTag);
    }
    
    @Test
    public void testNormalizeAttribute() {
        String attributeName = "Attribute";
        String normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("attribute", normalizedAttribute);
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Attribute1", "value1");
        attributes.put("Attribute2", "value2");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
    }
}