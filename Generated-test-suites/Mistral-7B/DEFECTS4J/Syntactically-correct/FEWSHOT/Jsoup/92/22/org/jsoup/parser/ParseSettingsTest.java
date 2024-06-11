package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        String attributeName = "id";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("id", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "id";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("id", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCaseTagAndAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "ID";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("ID", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.get("ID"));
    }

    @Test
    public void testNullSettings() {
        assertNull(ParseSettings.htmlDefault = null);
        assertNull(ParseSettings.preserveCase = null);
    }
}