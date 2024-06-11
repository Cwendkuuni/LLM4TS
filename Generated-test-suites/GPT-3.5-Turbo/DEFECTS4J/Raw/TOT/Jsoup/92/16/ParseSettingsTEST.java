import org.jsoup.nodes.Attributes;
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
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagWithPreserveTagCase() {
        parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeTagWithoutPreserveTagCase() {
        parseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);
    }

    @Test
    public void testNormalizeAttributeWithPreserveAttributeCase() {
        parseSettings = new ParseSettings(false, true);
        String attributeName = "HREF";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributeWithoutPreserveAttributeCase() {
        parseSettings = new ParseSettings(false, false);
        String attributeName = "HREF";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCase() {
        parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(attributes, normalizedAttributes);
    }

    @Test
    public void testNormalizeAttributesWithoutPreserveAttributeCase() {
        parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Attributes expectedNormalizedAttributes = new Attributes();
        expectedNormalizedAttributes.put("href", "https://www.example.com");
        assertEquals(expectedNormalizedAttributes, normalizedAttributes);
    }
}