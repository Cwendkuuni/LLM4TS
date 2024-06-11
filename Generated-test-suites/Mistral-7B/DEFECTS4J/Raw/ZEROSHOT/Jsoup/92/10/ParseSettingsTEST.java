package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

import static org.jsoup.internal.Normalizer.lowerCase;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultSettings() {
        ParseSettings parseSettings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String attributeName = "class";

        // Test normalizeTag method with HTMLDefault settings
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);

        // Test normalizeAttribute method with HTMLDefault settings
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);

        // Test normalizeAttributes method with HTMLDefault settings
        Attributes attributes = new Attributes();
        attributes.put("Class", "test");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings parseSettings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName = "style";

        // Test normalizeTag method with preserveCase settings
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        // Test normalizeAttribute method with preserveCase settings
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("style", normalizedAttributeName);

        // Test normalizeAttributes method with preserveCase settings
        Attributes attributes = new Attributes();
        attributes.put("Style", "color: red;");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("style", normalizedAttributes.get("style").getValue());
    }
}