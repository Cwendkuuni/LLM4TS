package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCaseAndAttributeCase_false_false() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "TAG";
        String attributeName = "ATTRIBUTE";

        // Test normalizeTag method
        assertEquals("tag", settings.normalizeTag(tagName));

        // Test normalizeAttribute method
        assertEquals("attribute", settings.normalizeAttribute(attributeName));

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("attribute", normalizedAttributes.get("attribute"));
    }

    @Test
    public void testPreserveTagCase_true_false() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";

        // Test normalizeTag method
        assertEquals("TAG", settings.normalizeTag(tagName));
    }

    @Test
    public void testPreserveAttributeCase_false_true() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "ATTRIBUTE";

        // Test normalizeAttribute method
        assertEquals("attribute", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testPreserveTagCaseAndAttributeCase_true_true() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "TAG";
        String attributeName = "ATTRIBUTE";

        // Test normalizeTag method
        assertEquals("TAG", settings.normalizeTag(tagName));

        // Test normalizeAttribute method
        assertEquals("ATTRIBUTE", settings.normalizeAttribute(attributeName));

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ATTRIBUTE", normalizedAttributes.get("ATTRIBUTE"));
    }

    @Test
    public void testNormalizeNullInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        // Test normalizeTag method with null input
        assertNull(settings.normalizeTag(null));

        // Test normalizeAttribute method with null input
        assertNull(settings.normalizeAttribute(null));

        // Test normalizeAttributes method with null input
        Attributes attributes = null;
        assertNull(settings.normalizeAttributes(attributes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParseSettingsConstruction() {
        new ParseSettings(true, false, 3); // Extra boolean argument
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParseSettingsConstruction_MissingBooleanArguments() {
        new ParseSettings(); // Missing preserveTagCase and preserveAttributeCase arguments
    }
}