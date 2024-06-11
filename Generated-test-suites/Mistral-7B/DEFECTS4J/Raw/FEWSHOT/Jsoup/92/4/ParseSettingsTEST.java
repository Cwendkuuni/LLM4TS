package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        attributes.put("CLASS", "foo");
        attributes.put("ID", "bar");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("class", normalizedAttributes.get("class").getValue());
        assertEquals("bar", normalizedAttributes.get("id").getValue());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        String attributeName = "ID";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("ID", normalizedAttributeName);

        Attributes attributes = new Attributes();
        attributes.put("ID", "bar");
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ID", normalizedAttributes.get("id").getValue());
        assertEquals("CLASS", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        tagName = "DIV";
        settings = new ParseSettings(true, false);
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);

        tagName = "DIV";
        settings = new ParseSettings(false, true);
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);

        tagName = "DIV";
        settings = new ParseSettings(true, true);
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("DIV", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "ID";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("id", normalizedAttributeName);

        attributeName = "ID";
        settings = new ParseSettings(true, false);
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("ID", normalizedAttributeName);

        attributeName = "ID";
        settings = new ParseSettings(false, true);
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("id", normalizedAttributeName);

        attributeName = "ID";
        settings = new ParseSettings(true, true);
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("ID", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "bar");
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("id", normalizedAttributes.get("id").getValue());
        assertEquals("class", normalizedAttributes.get("class").getValue());

        settings = new ParseSettings(true, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ID", normalizedAttributes.get("id").getValue());
        assertEquals("CLASS", normalizedAttributes.get("class").getValue());

        settings = new ParseSettings(false, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("id", normalizedAttributes.get("id").getValue());
        assertEquals("CLASS", normalizedAttributes.get("class").getValue());

        settings = new ParseSettings(true, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("ID", normalizedAttributes.get("id").getValue());
        assertEquals("CLASS", normalizedAttributes.get("class").getValue());
    }

    @Test
    public void testNullSettings() {
        ParseSettings settings = null;
        assertNull(settings);
        try {
            settings.normalizeTag(null);
        } catch (NullPointerException e) {
            // expected
        }
        try {
            settings.normalizeAttribute(null);
        } catch (NullPointerException e) {
            // expected
        }
        try {
            settings.normalizeAttributes(null);
        } catch (NullPointerException e) {
            // expected
        }
    }
}