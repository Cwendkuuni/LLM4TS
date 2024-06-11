package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultParseSettings() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        String tagName = "Div";
        String attributeName1 = "class";
        String attributeName2 = "id";

        String tagNameLowerCased = htmlDefault.normalizeTag(tagName);
        String attributeName1LowerCased = htmlDefault.normalizeAttribute(attributeName1);
        String attributeName2LowerCased = htmlDefault.normalizeAttribute(attributeName2);

        assertEquals("div", tagNameLowerCased);
        assertEquals("class", attributeName1LowerCased);
        assertEquals("id", attributeName2LowerCased);

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("id", "testId");

        Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
        String[] attributeNames = normalizedAttributes.keySet().toArray(new String[0]);

        Assert.assertEquals(2, attributeNames.length);
        Assert.assertEquals("class", attributeNames[0]);
        Assert.assertEquals("id", attributeNames[1]);
        Assert.assertEquals("testClass", normalizedAttributes.get("class"));
        Assert.assertEquals("testId", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCaseParseSettings() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "CLASS";
        String attributeName2 = "ID";

        String tagNameNormalized = preserveCase.normalizeTag(tagName);
        String attributeName1Normalized = preserveCase.normalizeAttribute(attributeName1);
        String attributeName2Normalized = preserveCase.normalizeAttribute(attributeName2);

        assertEquals("DIV", tagNameNormalized);
        assertEquals("CLASS", attributeName1Normalized);
        assertEquals("ID", attributeName2Normalized);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "testClass");
        attributes.put("ID", "testId");

        Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
        String[] attributeNames = normalizedAttributes.keySet().toArray(new String[0]);

        Assert.assertEquals("CLASS", attributeNames[0]);
        Assert.assertEquals("ID", attributeNames[1]);
        Assert.assertEquals("testClass", normalizedAttributes.get("CLASS"));
        Assert.assertEquals("testId", normalizedAttributes.get("ID"));
    }

    @Test
    public void testNullArguments() {
        ParseSettings preserveCase = ParseSettings.preserveCase;

        // Test normalizeTag() with a null tag name
        try {
            String tagNameNormalized = preserveCase.normalizeTag(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected, do nothing
        }

        // Test normalizeAttribute() with a null attribute name
        try {
            String attributeNameNormalized = preserveCase.normalizeAttribute(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected, do nothing
        }

        // Test normalizeAttributes() with a null Attributes object
        try {
            ParseSettings.htmlDefault.normalizeAttributes(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected, do nothing
        }
    }
}