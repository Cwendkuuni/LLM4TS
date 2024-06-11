package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "MyTag";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, tagName);
        assertEquals("mytag", normalizedTagName);

        settings = new ParseSettings(false, false);
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("mytag", normalizedTagName);

        settings = new ParseSettings(true, true);
        normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, false);
        String attributeName = "My-Attribute";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("my-attribute", normalizedAttributeName);

        settings = new ParseSettings(false, false);
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("my-attribute", normalizedAttributeName);

        settings = new ParseSettings(true, true);
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("my-attribute", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Id", "123");
        attributes.put("Class", "myclass");

        ParseSettings settings = new ParseSettings(true, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertSame("Id", normalizedAttributes.getKey(0));
        assertSame("123", normalizedAttributes.getValue(0));
        assertSame("class", normalizedAttributes.getKey(1));
        assertSame("myclass", normalizedAttributes.getValue(1));

        settings = new ParseSettings(false, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertSame("class", normalizedAttributes.getKey(0));
        assertSame("myclass", normalizedAttributes.getValue(0));
        assertSame("Id", normalizedAttributes.getKey(1));
        assertSame("123", normalizedAttributes.getValue(1));

        settings = new ParseSettings(true, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertSame("Id", normalizedAttributes.getKey(0));
        assertSame("123", normalizedAttributes.getValue(0));
        assertSame("Class", normalizedAttributes.getKey(1));
        assertSame("myclass", normalizedAttributes.getValue(1));
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributesNull() {
        ParseSettings settings = new ParseSettings(true, false);
        settings.normalizeAttributes(null);
    }
}