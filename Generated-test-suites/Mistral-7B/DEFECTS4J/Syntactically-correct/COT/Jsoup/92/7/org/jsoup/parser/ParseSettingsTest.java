package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "TagName";
        String attributeName = "AttributeName";

        // Test normalizeTag method
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("tagname", normalizedTag);

        // Test normalizeAttribute method
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        assertEquals("attributename", normalizedAttribute);

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("AttributeName", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("attributename", normalizedAttributes.get("attributeName").getValue());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "TagName";
        String attributeName = "AttributeName";

        // Test normalizeTag method
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("TagName", normalizedTag);

        // Test normalizeAttribute method
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        assertEquals("AttributeName", normalizedAttribute);

        // Test normalizeAttributes method
        Attributes attributes = new Attributes();
        attributes.put("AttributeName", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("AttributeName", normalizedAttributes.get("AttributeName").getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPreserveCaseSetting() {
        ParseSettings settings = new ParseSettings(false, true); // Invalid setting: preserveTagCase = false, preserveAttributeCase = true
        settings.normalizeTag("TagName");
    }

    @Test
    public void testNormalizeTagEmptyString() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "";
        String normalizedTag = settings.normalizeTag(tagName);
        assertEquals("", normalizedTag);
    }

    @Test
    public void testNormalizeTagNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = null;
        try {
            String normalizedTag = settings.normalizeTag(tagName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNormalizeAttributeEmptyString() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = "";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        assertEquals("", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributeNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = null;
        try {
            String normalizedAttribute = settings.normalizeAttribute(attributeName);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNormalizeAttributesNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = null;
        try {
            settings.normalizeAttributes(attributes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNormalizeAttributesEmptyAttributes() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertTrue(normalizedAttributes.isEmpty());
    }
}