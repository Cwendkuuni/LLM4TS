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
    public void testNormalizeTagWithPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        String result = parseSettings.normalizeTag(tagName);
        assertEquals(tagName, result);
    }

    @Test
    public void testNormalizeTagWithoutPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        String result = parseSettings.normalizeTag(tagName);
        assertEquals("div", result);
    }

    @Test
    public void testNormalizeAttributeWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "href";
        String result = parseSettings.normalizeAttribute(attributeName);
        assertEquals(attributeName, result);
    }

    @Test
    public void testNormalizeAttributeWithoutPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "HREF";
        String result = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", result);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        Attributes result = parseSettings.normalizeAttributes(attributes);
        assertNotNull(result);
        assertEquals("https://www.example.com", result.get("href"));
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes result = parseSettings.normalizeAttributes(attributes);
        assertNotNull(result);
        assertTrue(result.hasKey("HREF"));
        assertFalse(result.hasKey("href"));
        assertEquals("https://www.example.com", result.getIgnoreCase("HREF"));
    }
}