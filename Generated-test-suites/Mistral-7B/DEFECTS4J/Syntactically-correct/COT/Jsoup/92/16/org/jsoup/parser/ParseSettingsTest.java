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
        String tagName = "HTML";
        String attributeName = "class";

        String normalizedTag = settings.normalizeTag(tagName);
        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assertEquals("html", normalizedTag);
        assertEquals("class", normalizedAttribute);
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";

        String normalizedTag = settings.normalizeTag(tagName);

        assertEquals("DIV", normalizedTag);
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = "ID";

        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assertEquals("ID", normalizedAttribute);
    }

    @Test
    public void testPreserveCaseBoth() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "div";
        String attributeName = "ID";

        String normalizedTag = settings.normalizeTag(tagName);
        String normalizedAttribute = settings.normalizeAttribute(attributeName);

        assertEquals("DIV", normalizedTag);
        assertEquals("ID", normalizedAttribute);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeNullTag() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = null;

        settings.normalizeTag(tagName);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeNullAttribute() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = null;

        settings.normalizeAttribute(attributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("ID", "value");
        attributes.put("class", "classValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("value", normalizedAttributes.get("id"));
        assertEquals("classValue", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseNormalizeAttributes() {
        ParseSettings settings = ParseSettings.preserveCase;
        Attributes attributes = new Attributes();
        attributes.put("ID", "value");
        attributes.put("CLASS", "classValue");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("ID", normalizedAttributes.get("id"));
        assertEquals("CLASS", normalizedAttributes.get("class"));
    }
}