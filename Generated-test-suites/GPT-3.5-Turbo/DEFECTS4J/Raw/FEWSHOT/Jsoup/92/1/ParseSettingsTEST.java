package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ParseSettingsTest {
    private ParseSettings parseSettings;

    @Test
    public void testPreserveTagCase() {
        parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
        parseSettings = new ParseSettings(false, false);
        assertEquals(false, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        parseSettings = new ParseSettings(true, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
        parseSettings = new ParseSettings(false, true);
        assertEquals(false, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        parseSettings = new ParseSettings(true, false);
        assertEquals("tag", parseSettings.normalizeTag("TAG"));
        parseSettings = new ParseSettings(false, false);
        assertEquals("tag", parseSettings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeAttribute() {
        parseSettings = new ParseSettings(true, true);
        assertEquals("attribute", parseSettings.normalizeAttribute("ATTRIBUTE"));
        parseSettings = new ParseSettings(false, true);
        assertEquals("attribute", parseSettings.normalizeAttribute("ATTRIBUTE"));
    }

    @Test
    public void testNormalizeAttributes() {
        parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        attributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("value", attributes.get("ATTRIBUTE"));
        parseSettings = new ParseSettings(false, true);
        attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        attributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("value", attributes.get("attribute"));
    }
}