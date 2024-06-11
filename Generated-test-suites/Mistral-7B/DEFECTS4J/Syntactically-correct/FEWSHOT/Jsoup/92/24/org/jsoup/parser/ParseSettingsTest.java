package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testPreserveCaseValues() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        ParseSettings preserveCase = ParseSettings.preserveCase;

        assertEquals(false, htmlDefault.preserveTagCase());
        assertEquals(false, htmlDefault.preserveAttributeCase());

        assertEquals(true, preserveCase.preserveTagCase());
        assertEquals(true, preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        String tagName = "TAG";
        String normalizedTag = settings.normalizeTag(tagName);

        assertEquals(tagName, tagName);
        assertEquals("tag", normalizedTag);
    }

    @Test
    public void testNormalizeTagLowercase() {
        ParseSettings settings = ParseSettings.htmlDefault;

        String tagName = "TAG";
        String normalizedTag = settings.normalizeTag(tagName);

        assertEquals("tag", normalizedTag);
    }

    @Test
    public void testNormalizeTagNull() {
        ParseSettings settings = new ParseSettings(true, false);

        String tagName = null;

        assertNull(settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("FOO", attributes.get("foo").getValue());
        assertEquals("BAR", normalizedAttributes.get("foo").getValue());
    }

    @Test
    public void testNormalizeAttributesLowercase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("foo", attributes.get("foo").getValue());
        assertEquals("bar", normalizedAttributes.get("foo").getValue());
    }

    @Test
    public void testNormalizeAttributesNull() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = null;

        assertNull(settings.normalizeAttributes(attributes));
    }

    @Test
    public void testNormalizeAttributesSameInstance() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("foo", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertSame(attributes, normalizedAttributes);
    }
}