package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("   tag   "));
        assertEquals("tag", settings.normalizeTag("   TAG   "));

        settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("   tag   "));
        assertEquals("tag", settings.normalizeTag("   TAG   "));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute("attribute"));
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
        assertEquals("attribute", settings.normalizeAttribute("   attribute   "));
        assertEquals("attribute", settings.normalizeAttribute("   ATTRIBUTE   "));

        settings = new ParseSettings(false, false);
        assertEquals("attribute", settings.normalizeAttribute("attribute"));
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
        assertEquals("attribute", settings.normalizeAttribute("   attribute   "));
        assertEquals("attribute", settings.normalizeAttribute("   ATTRIBUTE   "));
    }

    @Test
    public void testNormalizeAttributes() {
        // Testing preserveAttributeCase = true
        Attributes attributes = new Attributes();
        attributes.put("Attribute", "Value");
        attributes.put("ATTRIBUTE", "VALUE");
        attributes.put("attribute", "value");

        ParseSettings settings = new ParseSettings(true, true);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("Value", normalizedAttributes.getIgnoreCase("Attribute"));
        assertEquals("VALUE", normalizedAttributes.getIgnoreCase("ATTRIBUTE"));
        assertEquals("value", normalizedAttributes.getIgnoreCase("attribute"));

        // Testing preserveAttributeCase = false
        attributes = new Attributes();
        attributes.put("Attribute", "Value");
        attributes.put("ATTRIBUTE", "VALUE");
        attributes.put("attribute", "value");

        settings = new ParseSettings(false, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertNull(normalizedAttributes.getIgnoreCase("Attribute"));
        assertEquals("Value", normalizedAttributes.getIgnoreCase("ATTRIBUTE"));
        assertEquals("value", normalizedAttributes.getIgnoreCase("attribute"));

        attributes = new Attributes();
        attributes.put("Attribute", "Value");
        attributes.put("ATTRIBUTE", "VALUE");
        attributes.put("attribute", "value");

        settings = new ParseSettings(false, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertNull(normalizedAttributes.getIgnoreCase("Attribute"));
        assertNull(normalizedAttributes.getIgnoreCase("ATTRIBUTE"));
        assertNull(normalizedAttributes.getIgnoreCase("attribute"));
    }
}