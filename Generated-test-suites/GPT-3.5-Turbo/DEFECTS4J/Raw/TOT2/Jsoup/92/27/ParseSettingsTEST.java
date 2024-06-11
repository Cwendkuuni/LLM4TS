package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = settings.normalizeTag("DIV");
        assertEquals("div", tagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = settings.normalizeAttribute("HREF");
        assertEquals("href", attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("SRC", "image.png");
        attributes.put("Title", "Example");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("src", normalizedAttributes.getKey("SRC"));
        assertEquals("title", normalizedAttributes.getKey("Title"));
    }

}