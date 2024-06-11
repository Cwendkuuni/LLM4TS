package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    // Test methods for constructor

    @Test
    public void testParseSettingsWithFalseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsWithTrueTrue() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    // Test methods for normalizeTag

    @Test
    public void testNormalizeTagWithLowerCaseInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String input = "html";
        String expected = "html";
        String result = settings.normalizeTag(input);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeTagWithUpperCaseInput() {
        ParseSettings settings = new ParseSettings(true, false);
        String input = "HTML";
        String expected = "HTML";
        String result = settings.normalizeTag(input);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeTagWithEmptyInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String input = "";
        String result = settings.normalizeTag(input);
        assertNull(result);
    }

    // Test methods for normalizeAttribute

    @Test
    public void testNormalizeAttributeWithLowerCaseInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String input = "class";
        String expected = "class";
        String result = settings.normalizeAttribute(input);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAttributeWithUpperCaseInput() {
        ParseSettings settings = new ParseSettings(true, true);
        String input = "CLASS";
        String expected = "CLASS";
        String result = settings.normalizeAttribute(input);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAttributeWithEmptyInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String input = "";
        String result = settings.normalizeAttribute(input);
        assertNull(result);
    }

    // Test method for normalizeAttributes

    @Test
    public void testNormalizeAttributesWithLowerCaseAttributes() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("class", "class");
        attributes.put("id", "id");

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("class", "class");
        expectedAttributes.put("id", "id");

        Attributes actualAttributes = settings.normalizeAttributes(attributes);
        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testNormalizeAttributesWithMixedCaseAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Class", "Class");
        attributes.put("Id", "id");

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("Class", "Class");
        expectedAttributes.put("Id", "id");

        Attributes actualAttributes = settings.normalizeAttributes(attributes);
        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testNormalizeAttributesWithNullInput() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = null;

        Attributes result = settings.normalizeAttributes(attributes);
        assertNull(result);
    }
}