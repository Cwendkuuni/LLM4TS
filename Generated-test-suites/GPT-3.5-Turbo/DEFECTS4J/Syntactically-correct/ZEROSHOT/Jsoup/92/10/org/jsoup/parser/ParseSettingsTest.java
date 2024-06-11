import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }
    
    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        String tagName = "DIV";
        String normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);
    }
    
    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HREF";
        String normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", normalizedAttribute);
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test-class");
        attributes.put("src", "test-src");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertTrue(normalizedAttributes.hasKey("class"));
        assertTrue(normalizedAttributes.hasKey("src"));
        assertFalse(normalizedAttributes.hasKey("CLASS"));
    }
}