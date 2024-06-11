package org.jsoup.parser;

import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());

        settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());

        settings = new ParseSettings(true, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        String name = settings.normalizeTag("DIV");
        assertEquals("DIV", name);

        name = settings.normalizeTag("div");
        assertEquals("div", name);

        settings = new ParseSettings(false, false);

        name = settings.normalizeTag("DIV");
        assertEquals("div", name);

        name = settings.normalizeTag("div");
        assertEquals("div", name);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        String name = settings.normalizeAttribute("HREF");
        assertEquals("href", name);

        name = settings.normalizeAttribute("href");
        assertEquals("href", name);

        settings = new ParseSettings(true, true);

        name = settings.normalizeAttribute("HREF");
        assertEquals("HREF", name);

        name = settings.normalizeAttribute("href");
        assertEquals("href", name);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("src", "image.jpg");

        Attributes normalizedAttr = settings.normalizeAttributes(attributes);
        assertTrue(normalizedAttr.hasKey("href"));
        assertTrue(normalizedAttr.hasKey("src"));
    }
}