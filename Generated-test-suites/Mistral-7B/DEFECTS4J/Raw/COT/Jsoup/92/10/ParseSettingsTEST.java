package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test cases for ParseSettings class
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNewParseSettingsTrueTrue() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNewParseSettingsFalseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testNewParseSettingsTrueFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testNewParseSettingsFalseTrue() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagLowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "MyTag";
        String expected = "mytag";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(expected, normalizedTagName);
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "MyTag";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeTagEmpty() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertNull(normalizedTagName);
    }

    @Test
    public void testNormalizeTagNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = null;
        String normalizedTagName = settings.normalizeTag(tagName);
        assertNull(normalizedTagName);
    }

    @Test
    public void testNormalizeAttributeLowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = "myattr";
        String expected = "myattr";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals(expected, normalizedAttrName);
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attrName = "MyATTR";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals(attrName, normalizedAttrName);
    }

    @Test
    public void testNormalizeAttributeEmpty() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = "";
        assertNull(settings.normalizeAttribute(attrName));
    }

    @Test
    public void testNormalizeAttributeNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attrName = null;
        assertNull(settings.normalizeAttribute(attrName));
    }

    @Test
    public void testNormalizeAttributesNullInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = null;
        assertNull(settings.normalizeAttributes(attributes));
    }

    @Test
    public void testNormalizeAttributesEmptyInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        assertNotNull(settings.normalizeAttributes(attributes));
    }
}