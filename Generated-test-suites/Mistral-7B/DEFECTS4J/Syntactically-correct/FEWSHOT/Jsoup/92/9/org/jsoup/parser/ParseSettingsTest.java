package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("HTML", normalizedTagName);

        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertSame(attributes, normalizedAttributes);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);

        tagName = "HTMLTAG";
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("HTMLTAG", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS", normalizedAttributeName);

        attributeName = "CLASS_TEST";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("CLASS_TEST", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test1");
        attributes.put("CLASS_TEST", "test2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("CLASS"));
        assertEquals("CLASS_TEST", normalizedAttributes.get("CLASS_TEST"));
    }

    @Test
    public void testNormalizeAttributesLowerCased() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test1");
        attributes.put("CLASS_TEST", "test2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("CLASS"));
        assertEquals("class_test", normalizedAttributes.get("CLASS_TEST"));
    }

    @Test
    public void testNull() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeTag(null));
        assertNull(settings.normalizeAttribute(null));
    }

    @Test
    public void testEmpty() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("", normalizedTagName);

        String attributeName = "";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("", normalizedAttributeName);

        Attributes attributes = new Attributes();
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertSame(attributes, normalizedAttributes);
    }
}