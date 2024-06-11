package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultParseSettings() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        String tagName = "DIV";
        String attributeName1 = "class";
        String attributeName2 = "data-custom";
        String tagNormalizedExpected = "div";
        String attributeName1NormalizedExpected = "class";
        String attributeName2NormalizedExpected = "data-custom";

        String tagNormalizedActual = htmlDefault.normalizeTag(tagName);
        String attributeName1NormalizedActual = htmlDefault.normalizeAttribute(attributeName1);
        String attributeName2NormalizedActual = htmlDefault.normalizeAttribute(attributeName2);

        assertEquals(tagNormalizedExpected, tagNormalizedActual);
        assertEquals(attributeName1NormalizedExpected, attributeName1NormalizedActual);
        assertEquals(attributeName2NormalizedExpected, attributeName2NormalizedActual);

        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("data-custom", "testData");

        Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);

        assertNotNull(normalizedAttributes);
        assertEquals("testclass", normalizedAttributes.get("class"));
        assertEquals("testdata", normalizedAttributes.get("data-custom"));
    }

    @Test
    public void testPreserveCaseParseSettings() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName1 = "CLASS";
        String attributeName2 = "DATA-CUSTOM";
        String tagNormalizedExpected = "DIV";
        String attributeName1NormalizedExpected = "CLASS";
        String attributeName2NormalizedExpected = "DATA-CUSTOM";

        String tagNormalizedActual = preserveCase.normalizeTag(tagName);
        String attributeName1NormalizedActual = preserveCase.normalizeAttribute(attributeName1);
        String attributeName2NormalizedActual = preserveCase.normalizeAttribute(attributeName2);

        assertEquals(tagNormalizedExpected, tagNormalizedActual);
        assertEquals(attributeName1NormalizedExpected, attributeName1NormalizedActual);
        assertEquals(attributeName2NormalizedExpected, attributeName2NormalizedActual);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "testClass");
        attributes.put("DATA-CUSTOM", "testData");

        Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);

        assertNotNull(normalizedAttributes);
        assertEquals("CLASS", normalizedAttributes.get("class"));
        assertEquals("DATA-CUSTOM", normalizedAttributes.get("data-custom"));
    }
}