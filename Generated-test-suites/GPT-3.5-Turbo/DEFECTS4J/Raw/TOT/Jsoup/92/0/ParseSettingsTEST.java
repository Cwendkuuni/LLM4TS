package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
        
        parseSettings = new ParseSettings(false, true);
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());

        parseSettings = new ParseSettings(true, false);
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));

        parseSettings = new ParseSettings(false, true);
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HREF";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));

        parseSettings = new ParseSettings(true, false);
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        attributes.put("title", "Example");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("Example", normalizedAttributes.get("title"));

        parseSettings = new ParseSettings(false, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("Example", normalizedAttributes.get("title"));
    }
}