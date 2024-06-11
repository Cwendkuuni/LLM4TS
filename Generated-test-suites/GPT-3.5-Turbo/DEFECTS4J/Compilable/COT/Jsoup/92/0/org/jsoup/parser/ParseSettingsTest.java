package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("tagname", parseSettings.normalizeTag("tagName"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("attribute", parseSettings.normalizeAttribute("Attribute"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);

        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
    }
}
