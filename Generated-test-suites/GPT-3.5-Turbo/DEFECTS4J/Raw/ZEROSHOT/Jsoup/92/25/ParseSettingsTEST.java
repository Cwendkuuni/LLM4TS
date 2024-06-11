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
        ParseSettings parseSettings = new ParseSettings(false, false);

        String tagName = "div";
        String normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTag);

        parseSettings = new ParseSettings(true, false);

        tagName = "DIV";
        normalizedTag = parseSettings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);

        String attributeName = "class";
        String normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttribute);

        parseSettings = new ParseSettings(false, true);

        attributeName = "CLASS";
        normalizedAttribute = parseSettings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("title", "example");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("class", normalizedAttributes.asList().get(0).getKey());
        assertEquals("test", normalizedAttributes.asList().get(0).getValue());
        assertEquals("title", normalizedAttributes.asList().get(1).getKey());
        assertEquals("example", normalizedAttributes.asList().get(1).getValue());
    }
}