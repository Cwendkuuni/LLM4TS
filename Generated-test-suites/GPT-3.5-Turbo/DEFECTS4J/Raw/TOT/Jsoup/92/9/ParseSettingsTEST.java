import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, false);
    }
    
    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
        
        parseSettings = new ParseSettings(false, false);
        assertFalse(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
        
        parseSettings = new ParseSettings(true, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        String tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));
        
        parseSettings = new ParseSettings(false, false);
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }
    
    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HtTp-EQuIv";
        assertEquals("HtTp-EQuIv", parseSettings.normalizeAttribute(attributeName));
        
        parseSettings = new ParseSettings(true, false);
        assertEquals("http-equiv", parseSettings.normalizeAttribute(attributeName));
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HtTp-EQuIv", "Content-Type");
        attributes.put("Id", "example-id");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals("Content-Type", normalizedAttributes.getIgnoreCase("HtTp-EQuIv"));
        assertEquals("example-id", normalizedAttributes.getIgnoreCase("Id"));
        
        parseSettings = new ParseSettings(true, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals("Content-Type", normalizedAttributes.getIgnoreCase("HtTp-EQuIv"));
        assertEquals("example-id", normalizedAttributes.getIgnoreCase("Id"));
    }
}