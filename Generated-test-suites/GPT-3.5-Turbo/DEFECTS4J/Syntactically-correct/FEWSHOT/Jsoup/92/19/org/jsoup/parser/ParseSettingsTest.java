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
        assertEquals("div", parseSettings.normalizeTag("div"));
        assertEquals("p", parseSettings.normalizeTag("P"));
        assertEquals("span", parseSettings.normalizeTag(" SPAN "));
        assertEquals("a", parseSettings.normalizeTag("A"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("class", parseSettings.normalizeAttribute("class"));
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
        assertEquals("name", parseSettings.normalizeAttribute(" NAME "));
        assertEquals("href", parseSettings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "example");
        attributes.put("iD", "id-123");
        attributes.put("value", "test");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("class", normalizedAttributes.getKey("CLASS"));
        assertEquals("id", normalizedAttributes.getKey("iD"));
        assertEquals("value", normalizedAttributes.getKey("value"));
        assertEquals("example", normalizedAttributes.getValue("class"));
        assertEquals("id-123", normalizedAttributes.getValue("id"));
        assertEquals("test", normalizedAttributes.getValue("value"));
    }
}