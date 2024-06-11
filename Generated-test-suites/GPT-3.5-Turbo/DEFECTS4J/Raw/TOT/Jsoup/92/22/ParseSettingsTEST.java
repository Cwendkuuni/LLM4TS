import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void testNormalizeTag_withPreserveTagCase() {
        parseSettings = new ParseSettings(true, false);
        assertEquals("h1", parseSettings.normalizeTag("H1"));
    }

    @Test
    public void testNormalizeTag_withoutPreserveTagCase() {
        assertEquals("h1", parseSettings.normalizeTag("H1"));
    }

    @Test
    public void testNormalizeAttribute_withPreserveAttributeCase() {
        parseSettings = new ParseSettings(false, true);
        assertEquals("href", parseSettings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttribute_withoutPreserveAttributeCase() {
        assertEquals("href", parseSettings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes_withPreserveAttributeCase() {
        parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        parseSettings.normalizeAttributes(attributes);
        assertTrue(attributes.hasKey("HREF"));
    }

    @Test
    public void testNormalizeAttributes_withoutPreserveAttributeCase() {
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        parseSettings.normalizeAttributes(attributes);
        assertFalse(attributes.hasKey("HREF"));
        assertTrue(attributes.hasKey("href"));
    }

}