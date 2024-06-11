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
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("html", parseSettings.normalizeTag("HTML"));
        assertEquals("p", parseSettings.normalizeTag("p"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
        assertEquals("class", parseSettings.normalizeAttribute("CLASS"));
        assertEquals("src", parseSettings.normalizeAttribute("src"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        attributes.put("CLASS", "test");
        attributes.put("src", "image.jpg");

        parseSettings.normalizeAttributes(attributes);

        assertEquals("123", attributes.get("id"));
        assertEquals("test", attributes.get("class"));
        assertEquals("image.jpg", attributes.get("src"));
    }
}