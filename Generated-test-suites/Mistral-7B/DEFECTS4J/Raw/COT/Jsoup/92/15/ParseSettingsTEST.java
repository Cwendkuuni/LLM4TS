package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);

        String attributeName = "COLOR";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("color", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("COLOR", "RED");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("color", normalizedAttributes.get("color").getKey());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.get("CLASS").getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForPreserveTagCaseWithFalseAndPreserveAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(false, true);
        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        // This test should throw an IllegalArgumentException
        settings.normalizeTag(tagName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForPreserveTagCaseTrueAndPreserveAttributeCaseWithFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        // This test should throw an IllegalArgumentException
        settings.normalizeAttribute(attributeName);
    }
}