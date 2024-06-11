package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

/**
 * tests for ParseSettings class
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String expectedLowerTagName = "html";
        String attributeName = "class";
        String expectedLowerAttributeName = "class";

        String tagNameNormalized = settings.normalizeTag(tagName);
        String attributeNameNormalized = settings.normalizeAttribute(attributeName);

        assertEquals(expectedLowerTagName, tagNameNormalized);
        assertEquals(expectedLowerAttributeName, attributeNameNormalized);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeValue = normalizedAttributes.get("class");
        assertEquals(expectedLowerAttributeName, normalizedAttributeValue);
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String expectedTagName = "DIV";
        String attributeName = "class";
        String expectedAttributeName = "class";

        String tagNameNormalized = settings.normalizeTag(tagName);
        String attributeNameNormalized = settings.normalizeAttribute(attributeName);

        assertEquals(expectedTagName, tagNameNormalized);
        assertEquals(expectedAttributeName, attributeNameNormalized);

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeValue = normalizedAttributes.get("class");
        assertEquals(expectedAttributeName, normalizedAttributeValue);
    }

    @Test(expected = AssertionError.class)
    public void testPreserveTagCaseWithLowerInput() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "HTML";
        String tagNameNormalized = settings.normalizeTag(tagName);

        assertEquals("HTML", tagNameNormalized); //this test should pass
        // this test should fail because input is lower-case and preserveTagCase is set to true
        String invalidTagName = "html";
        String expectedTagName = "HTML";
        String tagNameNormalizedInvalid = settings.normalizeTag(invalidTagName);

        assertEquals(expectedTagName, tagNameNormalizedInvalid); //this assertion will fail
    }

    @Test(expected = AssertionError.class)
    public void testPreserveAttributeCaseWithLowerInput() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "Class";
        String attributeNameNormalized = settings.normalizeAttribute(attributeName);

        assertEquals("class", attributeNameNormalized); // this test should pass
        // this test should fail because input is lower-case and preserveAttributeCase is set to true
        String invalidAttributeName = "class";
        String expectedAttributeName = "Class";
        String attributeNameNormalizedInvalid = settings.normalizeAttribute(invalidAttributeName);

        assertEquals(expectedAttributeName, attributeNameNormalizedInvalid); //this assertion will fail
    }
}