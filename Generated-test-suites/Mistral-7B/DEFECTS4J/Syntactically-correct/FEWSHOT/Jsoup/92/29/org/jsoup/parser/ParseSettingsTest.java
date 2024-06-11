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

        String attributeName = "class";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getClass().toString());
        assertEquals("foo", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseForTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "class";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getClass().toString());
        assertEquals("foo", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCaseForAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.getClass().toString());
        assertEquals("foo", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testPreserveCaseForBothTagAndAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.getClass().toString());
        assertEquals("foo", normalizedAttributes.get("CLASS"));
    }
}