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
        ParseSettings settings = new ParseSettings(true, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_noPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeTag_preserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("DIV", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute_noPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttribute_preserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes_noPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value");
        assertEquals("class", settings.normalizeAttributes(attributes).get("CLASS"));
    }

    @Test
    public void testNormalizeAttributes_preserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value");
        assertEquals("CLASS", settings.normalizeAttributes(attributes).get("CLASS"));
    }
}