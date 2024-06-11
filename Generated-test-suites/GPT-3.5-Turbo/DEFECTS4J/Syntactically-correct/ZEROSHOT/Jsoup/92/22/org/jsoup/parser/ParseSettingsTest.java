import org.junit.Before;
import org.junit.Test;

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
        String expected = "div";
        assertEquals(expected, parseSettings.normalizeTag(tagName));
        
        tagName = "a";
        expected = "a";
        assertEquals(expected, parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HREF";
        String expected = "href";
        assertEquals(expected, parseSettings.normalizeAttribute(attributeName));
        
        attributeName = "src";
        expected = "src";
        assertEquals(expected, parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        attributes.put("src", "image.jpg");

        Attributes normalized = parseSettings.normalizeAttributes(attributes);

        assertTrue(normalized.hasKey("href"));
        assertTrue(normalized.hasKey("src"));
    }
}