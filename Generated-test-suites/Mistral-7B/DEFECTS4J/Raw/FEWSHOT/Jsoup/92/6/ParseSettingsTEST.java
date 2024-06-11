package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

        String attrName = "class";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("class", normalizedAttrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("ID", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("bar", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attrName = "class";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("class", normalizedAttrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("ID", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("ID", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attrName = "class";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("class", normalizedAttrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("ID", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("ID", normalizedAttributes.get("id"));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        String attrName = "CLASS";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("CLASS", normalizedAttrName);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("ID", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class"));
        assertEquals("ID", normalizedAttributes.get("id"));
    }

    @Test
    public void testNullSettings() {
        assertNull(ParseSettings.htmlDefault = null);
        assertNull(ParseSettings.preserveCase = null);
    }
}