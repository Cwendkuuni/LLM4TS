package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertNotNull(settings);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertNotNull(settings);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsNewInstancePreserveTagCaseTrue() {
        ParseSettings settings = new ParseSettings(true, false);
        assertNotNull(settings);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsNewInstancePreserveTagCaseFalse() {
        ParseSettings settings = new ParseSettings(false, true);
        assertNotNull(settings);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String normalizeTag = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizeTag);

        tagName = "tag";
        normalizeTag = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizeTag);

        tagName = "Tag";
        normalizeTag = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizeTag);

        settings = ParseSettings.htmlDefault;
        tagName = "TAG";
        normalizeTag = settings.normalizeTag(tagName);
        assertEquals("tag", normalizeTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "ATTRIBUTE";
        String normalizeAttribute = settings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizeAttribute);

        attributeName = "attribute";
        normalizeAttribute = settings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizeAttribute);

        attributeName = "ATTRIBUTE";
        normalizeAttribute = ParseSettings.htmlDefault.normalizeAttribute(attributeName);
        assertEquals("attribute", normalizeAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attributeOne", "valueOne");
        attributes.put("attributeTwo", "valueTwo");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attributeone", normalizedAttributes.get("attributeOne").getName());
        assertEquals("attributetwo", normalizedAttributes.get("attributeTwo").getName());

        settings = ParseSettings.htmlDefault;
        attributes = new Attributes();
        attributes.put("attributeOne", "valueOne");
        attributes.put("attributeTwo", "valueTwo");

        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attributeone", normalizedAttributes.get("attributeOne").getName());
        assertEquals("attributetwo", normalizedAttributes.get("attributeTwo").getName());
    }
}