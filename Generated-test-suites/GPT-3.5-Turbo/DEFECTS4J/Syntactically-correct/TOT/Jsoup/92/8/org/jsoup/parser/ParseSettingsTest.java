package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        // Check if preserveTagCase returns true for preserveCase
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        // Check if preserveAttributeCase returns true for preserveCase
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveTagCaseFalse() {
        // Check if preserveTagCase returns false for htmlDefault
        ParseSettings settings = ParseSettings.htmlDefault;
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCaseFalse() {
        // Check if preserveAttributeCase returns false for htmlDefault
        ParseSettings settings = ParseSettings.htmlDefault;
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        // Check if normalizeTag preserves case when preserveTagCase is true
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "div";
        assertEquals("div", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTagLowercase() {
        // Check if normalizeTag converts tag name to lowercase when preserveTagCase is false
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        assertEquals("div", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        // Check if normalizeAttribute preserves case when preserveAttributeCase is true
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "href";
        assertEquals("href", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributeLowercase() {
        // Check if normalizeAttribute converts attribute name to lowercase when preserveAttributeCase is false
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "HREF";
        assertEquals("href", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        // Check if normalizeAttributes does not modify attribute names when preserveAttributeCase is true
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("Href", "http://example.com");
        attributes.put("SRC", "image.jpg");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("http://example.com", normalizedAttributes.get("Href"));
        assertEquals("image.jpg", normalizedAttributes.get("SRC"));
    }

    @Test
    public void testNormalizeAttributesLowercase() {
        // Check if normalizeAttributes converts attribute names to lowercase when preserveAttributeCase is false
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("Href", "http://example.com");
        attributes.put("SRC", "image.jpg");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("http://example.com", normalizedAttributes.get("href"));
        assertEquals("image.jpg", normalizedAttributes.get("src"));
    }
}