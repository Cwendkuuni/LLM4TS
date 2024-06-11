package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String attributeName = "class";
        String expectedTagName = "html";
        String expectedAttributeName = "class";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("test", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String attributeName = "ID";
        String expectedTagName = "DIV";
        String expectedAttributeName = "ID";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.get("ID").getName());
        assertEquals("test", normalizedAttributes.get("ID").getValue());
    }

    @Test
    public void testPreserveTagCaseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        String attributeName = "class";
        String expectedTagName = "div";
        String expectedAttributeName = "class";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class").getName());
        assertEquals("test", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testPreserveAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "DIV";
        String attributeName = "ID";
        String expectedTagName = "DIV";
        String expectedAttributeName = "id";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("ID").getName());
        assertEquals("test", normalizedAttributes.get("ID").getValue());
    }

    @Test
    public void testPreserveTagCaseAndAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        String attributeName = "ID";
        String expectedTagName = "div";
        String expectedAttributeName = "id";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("ID").getName());
        assertEquals("test", normalizedAttributes.get("ID").getValue());
    }

    @Test
    public void testPreserveTagCaseAndAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = "DIV";
        String attributeName = "ID";
        String expectedTagName = "DIV";
        String expectedAttributeName = "ID";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.get("ID").getName());
        assertEquals("test", normalizedAttributes.get("ID").getValue());
    }
}