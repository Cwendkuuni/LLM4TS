package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
        String tagName = "htmlElement";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("htmlelement", normalizedTagName);
        String attrName = "customAttribute";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("customattribute", normalizedAttrName);
        Attributes attributes = new Attributes();
        attributes.put("customAttribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("customattribute", normalizedAttributes.get("customAttribute").getValue());
    }

    @Test
    public void testParseSettingsPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
        String tagName = "HTML_Element";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("HTML_Element", normalizedTagName);
        String attrName = "customAttribute";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("customattribute", normalizedAttrName);
        Attributes attributes = new Attributes();
        attributes.put("customAttribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("customAttribute", normalizedAttributes.get("customAttribute").getValue());
    }

    @Test
    public void testParseSettingsPreserveCaseTagAndAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
        String tagName = "HTML_Element";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("HTML_Element", normalizedTagName);
        String attrName = "customAttribute";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("customattribute", normalizedAttrName);
        Attributes attributes = new Attributes();
        attributes.put("customAttribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("customAttribute", normalizedAttributes.get("customAttribute").getValue());
    }

    @Test
    public void testParseSettingsPreserveAttributeCaseNull() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertNull(settings.normalizeAttributes(null));
    }
}