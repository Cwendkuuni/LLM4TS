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
    public void testNormalizeTag() {
        String tagName = " DIV ";
        String normalized = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalized);
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = " CLASS ";
        String normalized = parseSettings.normalizeAttribute(attributeName);
        assertEquals("class", normalized);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put(" HREF ", "https://www.example.com");
        attributes.put(" CLASS ", " active ");
        
        Attributes normalized = parseSettings.normalizeAttributes(attributes);
        assertTrue(normalized.hasKey("href"));
        assertTrue(normalized.hasKey("class"));
        
        assertEquals("https://www.example.com", normalized.get("href"));
        assertEquals("active", normalized.get("class"));
    }
}