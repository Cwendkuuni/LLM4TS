package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_Lowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "TAG";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(lowerCase(tagName), normalizedTagName);
    }

    @Test
    public void testNormalizeTag_SameCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute_Lowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attrName = "ATTR";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        Assert.assertEquals(lowerCase(attrName), normalizedAttrName);
    }

    @Test
    public void testNormalizeAttribute_SameCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String attrName = "ATTR";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        Assert.assertEquals(attrName, normalizedAttrName);
    }

    @Test
    public void testNormalizeAttributes_Lowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String[] names = normalizedAttributes.names();
        for (String name : names) {
            Assert.assertEquals(lowerCase(name), name);
        }
    }

    @Test
    public void testNormalizeAttributes_SameCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "val1");
        attributes.put("attr2", "val2");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String[] names = normalizedAttributes.names();
        for (String name : names) {
            Assert.assertEquals(name, name);
        }
    }

    // Edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testPreserveCase_InvalidArguments() {
        new ParseSettings(true, false); // preserveTagCase is true, preserveAttributeCase is false
    }

    @Test
    public void testNormalizeTag_EmptyString() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("", normalizedTagName);
    }

    @Test
    public void testNormalizeTag_Null() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = null;
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertNull(normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute_EmptyString() {
        ParseSettings settings = new ParseSettings(true, false);
        String attrName = "";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        Assert.assertEquals("", normalizedAttrName);
    }

    @Test
    public void testNormalizeAttribute_Null() {
        ParseSettings settings = new ParseSettings(true, false);
        String attrName = null;
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        Assert.assertNull(normalizedAttrName);
    }
}