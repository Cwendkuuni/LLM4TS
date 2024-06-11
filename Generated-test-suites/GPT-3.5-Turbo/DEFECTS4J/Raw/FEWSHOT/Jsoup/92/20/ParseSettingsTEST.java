package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());

        parseSettings = new ParseSettings(false, false);
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("tag", parseSettings.normalizeTag("Tag"));

        parseSettings = new ParseSettings(false, false);
        assertEquals("tag", parseSettings.normalizeTag("Tag"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("attribute", parseSettings.normalizeAttribute("Attribute"));

        parseSettings = new ParseSettings(false, false);
        assertEquals("attribute", parseSettings.normalizeAttribute("Attribute"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertTrue(normalizedAttributes.hasKey("attribute"));

        parseSettings = new ParseSettings(false, false);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertFalse(normalizedAttributes.hasKey("attribute"));
    }
}