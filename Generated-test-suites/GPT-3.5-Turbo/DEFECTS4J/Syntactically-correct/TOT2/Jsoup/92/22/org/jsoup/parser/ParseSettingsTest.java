package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("title", parseSettings.normalizeTag("TITLE"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
    }
}