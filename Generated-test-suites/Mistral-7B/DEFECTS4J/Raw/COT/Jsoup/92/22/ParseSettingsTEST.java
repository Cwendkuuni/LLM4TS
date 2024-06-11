package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_lowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("html", normalizedTagName);
    }

    @Test
    public void testNormalizeTag_preserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "HTML";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute_lowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals("class", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttribute_preserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attributeName = "CLASS";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        assertEquals(attributeName, normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes_lowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        attributes.put("ID", "id");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.keyAt(0));
        assertEquals("test", normalizedAttributes.valueAt(0));
        assertNull(normalizedAttributes.keyAt(1));
        assertNull(normalizedAttributes.valueAt(1));
        assertEquals("id", normalizedAttributes.keyAt(1));
        assertEquals("id", normalizedAttributes.valueAt(1));
    }

    @Test
    public void testNormalizeAttributes_preserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test");
        attributes.put("ID", "id");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.keyAt(0));
        assertEquals("test", normalizedAttributes.valueAt(0));
        assertEquals("ID", normalizedAttributes.keyAt(1));
        assertEquals("id", normalizedAttributes.valueAt(1));
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeTag_null() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = null;
        String normalizedTagName = settings.normalizeTag(tagName);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttribute_null() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attributeName = null;
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
    }
}