import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = parseSettings.normalizeTag("DIV");
        assertEquals("DIV", tagName);
    }

    @Test
    public void testNormalizeTagLowercase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = parseSettings.normalizeTag("DIV");
        assertEquals("div", tagName);
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = parseSettings.normalizeAttribute("HREF");
        assertEquals("HREF", attributeName);
    }

    @Test
    public void testNormalizeAttributeLowercase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = parseSettings.normalizeAttribute("HREF");
        assertEquals("href", attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("HREF", normalizedAttributes.get("HREF"));
    }
}