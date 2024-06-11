import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }
    
    @Test
    public void testPreserveTagCase() {
        assertEquals(false, parseSettings.preserveTagCase());
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, parseSettings.preserveAttributeCase());
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        assertEquals("title", parseSettings.normalizeTag(" TITLE "));
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("TITLE", parseSettings.normalizeTag(" TITLE "));
    }
    
    @Test
    public void testNormalizeAttribute() {
        assertEquals("href", parseSettings.normalizeAttribute(" HREF "));
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("href", parseSettings.normalizeAttribute(" HREF "));
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(" HREF ", "http://example.com");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("http://example.com", normalizedAttributes.get("href"));
        
        ParseSettings parseSettings = new ParseSettings(false, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("http://example.com", normalizedAttributes.get(" HREF "));
    }
    
}