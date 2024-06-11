package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("tag", parseSettings.normalizeTag("Tag"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("attribute", parseSettings.normalizeAttribute("Attribute"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("Attribute1", "Value1");
        attributes.put("Attribute2", "Value2");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("attribute1", normalizedAttributes.get("Attribute1"));
        assertEquals("attribute2", normalizedAttributes.get("Attribute2"));
    }
}