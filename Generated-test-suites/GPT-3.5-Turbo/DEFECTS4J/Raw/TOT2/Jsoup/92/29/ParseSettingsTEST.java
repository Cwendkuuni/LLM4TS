package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("href", settings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("id", "testID");
        settings.normalizeAttributes(attributes);
        assertFalse(attributes.hasKey("ID"));
    }
}