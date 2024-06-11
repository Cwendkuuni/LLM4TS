package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("HTML");
        String attributeName = settings.normalizeAttribute("class");
        String expectedTagName = "html";
        String expectedAttributeName = "class";
        assertEquals(expectedTagName, tagName);
        assertEquals(expectedAttributeName, attributeName);
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = settings.normalizeTag("DIV");
        String expectedTagName = "DIV";
        assertEquals(expectedTagName, tagName);
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = settings.normalizeAttribute("ID");
        String expectedAttributeName = "ID";
        assertEquals(expectedAttributeName, attributeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSettingsNullTag() {
        ParseSettings settings = new ParseSettings(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSettingsNullAttribute() {
        ParseSettings settings = new ParseSettings(false, null);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("ID", "testID");
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String expectedClass = "testclass";
        String expectedID = "testid";
        assertEquals(expectedClass, normalizedAttributes.getValue("class"));
        assertEquals(expectedID, normalizedAttributes.getValue("ID"));
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "testClass");
        attributes.put("ID", "testID");
        ParseSettings settings = new ParseSettings(true, true);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String expectedClass = "CLASS";
        String expectedID = "ID";
        assertEquals(expectedClass, normalizedAttributes.getKey("class"));
        assertEquals(expectedID, normalizedAttributes.getKey("ID"));
    }
}