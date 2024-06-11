package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        Attributes attributes = new Attributes();
        attributes.put("class", "myclass");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.keySet().toString());
        assertEquals("myclass", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String attributeName = "My-Attribute";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("My-Attribute", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("My-Attribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("My-Attribute", normalizedAttributes.keySet().toString());
        assertEquals("value", normalizedAttributes.get("My-Attribute"));
    }

    @Test
    public void testPreserveCaseBoth() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "My-Attribute";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("My-Attribute", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("My-Attribute", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("My-Attribute", normalizedAttributes.keySet().toString());
        assertEquals("value", normalizedAttributes.get("My-Attribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new ParseSettings(true, false); // Preserve tag case but not attribute case
    }
}