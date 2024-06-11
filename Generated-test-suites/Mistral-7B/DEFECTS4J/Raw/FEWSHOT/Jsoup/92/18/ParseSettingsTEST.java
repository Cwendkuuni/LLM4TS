package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "HTML";
        String tagNameLower = settings.normalizeTag(tagName);
        assertEquals("html", tagNameLower);

        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "HTML";
        String tagNameNormalized = settings.normalizeTag(tagName);
        assertNull(tagNameNormalized);
        assertEquals("HTML", tagNameNormalized);

        Attributes attributes = new Attributes();
        attributes.put("ID", "testId");
        attributes.put("class", "testClass");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ID", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "HTML";
        String tagNameNormalized = settings.normalizeTag(tagName);
        assertNull(tagNameNormalized);
        assertEquals("html", settings.normalizeTag("html"));
        assertEquals("HTML", settings.normalizeTag("HTML"));
        assertEquals("Tag", settings.normalizeTag("Tag"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "id";
        String attributeNameNormalized = settings.normalizeAttribute(attributeName);
        assertEquals("id", attributeNameNormalized);

        String attributeNameWithDashes = "ID";
        String attributeNameWithDashesNormalized = settings.normalizeAttribute(attributeNameWithDashes);
        assertEquals("id", attributeNameWithDashesNormalized);

        String attributeNameWithLeadingAndTrailingQuotes = "\"id\"";
        String attributeNameWithLeadingAndTrailingQuotesNormalized = settings.normalizeAttribute(attributeNameWithLeadingAndTrailingQuotes);
        assertEquals("id", attributeNameWithLeadingAndTrailingQuotesNormalized);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));

        attributes.put("ID", "TestId");
        attributes.put("CLASS", "TestClass");

        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testNormalizeAttributesCaseInsensitive() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("id", "testId");
        attributes.put("class", "testClass");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));

        attributes.put("ID", "TestId");
        attributes.put("CLASS", "TestClass");

        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));
    }
}