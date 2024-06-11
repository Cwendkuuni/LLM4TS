package org.jsoup.parser;

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
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        String tag = "div";
        assertEquals(tag, parseSettings.normalizeTag(tag));

        parseSettings = new ParseSettings(true, false);
        assertEquals(tag, parseSettings.normalizeTag(tag.toUpperCase()));
    }

    @Test
    public void testNormalizeAttribute() {
        String attribute = "href";
        assertEquals(attribute, parseSettings.normalizeAttribute(attribute));

        parseSettings = new ParseSettings(false, true);
        assertEquals(attribute, parseSettings.normalizeAttribute(attribute.toUpperCase()));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(attributes, normalizedAttributes);

        parseSettings = new ParseSettings(false, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertFalse(attributes.equals(normalizedAttributes));
        assertTrue(normalizedAttributes.hasKey("href"));
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
    }
}