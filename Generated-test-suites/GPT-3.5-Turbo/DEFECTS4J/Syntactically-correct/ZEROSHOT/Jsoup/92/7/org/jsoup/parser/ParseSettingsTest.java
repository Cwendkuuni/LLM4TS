package org.jsoup.parser;

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
        String tagName = parseSettings.normalizeTag("div");
        assertEquals("div", tagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = parseSettings.normalizeAttribute("class");
        assertEquals("class", attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("class", "value");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getKey(0));
        assertEquals("value", normalizedAttributes.getValue(0));
    }
}