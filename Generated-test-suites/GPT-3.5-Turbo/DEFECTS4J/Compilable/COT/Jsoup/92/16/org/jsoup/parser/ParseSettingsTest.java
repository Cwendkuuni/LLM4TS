package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        boolean result = parseSettings.preserveTagCase();
        assertTrue(result);
    }

    @Test
    public void testNormalizeTag() {
        String name = "div";
        String normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(name, normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        String name = "class";
        String normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(name, normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("id", "test");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.asList().get(0).getKey());
        assertEquals("id", normalizedAttributes.asList().get(1).getKey());
    }
}