package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setup() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("span", parseSettings.normalizeTag("SPAN"));
        assertEquals("h1", parseSettings.normalizeTag("h1"));
        assertEquals("a", parseSettings.normalizeTag("A"));
        assertEquals("img", parseSettings.normalizeTag("IMG"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("src", parseSettings.normalizeAttribute("SRC"));
        assertEquals("alt", parseSettings.normalizeAttribute("ALT"));
        assertEquals("title", parseSettings.normalizeAttribute("title"));
        assertEquals("class", parseSettings.normalizeAttribute("Class"));
        assertEquals("data-id", parseSettings.normalizeAttribute("DATA-ID"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("SRC", "image.jpg");
        attributes.put("ALT", "Image");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("image.jpg", normalizedAttributes.get("src"));
        assertEquals("Image", normalizedAttributes.get("alt"));
    }
}