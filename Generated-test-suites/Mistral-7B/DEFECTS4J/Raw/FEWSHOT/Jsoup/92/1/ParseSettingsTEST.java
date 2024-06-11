package org.jsoup.parser;

import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultSettings() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        Assert.assertFalse(htmlDefault.preserveTagCase());
        Assert.assertFalse(htmlDefault.preserveAttributeCase());
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        Assert.assertTrue(preserveCase.preserveTagCase());
        Assert.assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);

        tagName = "tag";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals(tagName, normalizedTagName);

        settings = new ParseSettings(false, false);
        tagName = "TAG";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("tag", normalizedTagName);

        tagName = "tag";
        normalizedTagName = settings.normalizeTag(tagName);
        Assert.assertEquals("tag", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String attributeName = "ATTR";
        String normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttributeName);

        attributeName = "Attr";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals(attributeName, normalizedAttributeName);

        settings = new ParseSettings(true, false);
        attributeName = "ATTR";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);

        attributeName = "Attr";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);

        settings = new ParseSettings(false, true);
        attributeName = "ATTR";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);

        attributeName = "Attr";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);

        settings = new ParseSettings(false, false);
        attributeName = "ATTR";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);

        attributeName = "Attr";
        normalizedAttributeName = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("attr", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("Attr1", "value2");
        attributes.put("ATTR1", "value3");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(3, normalizedAttributes.size());
        Assert.assertEquals("attr1", normalizedAttributes.get("attr1").getKey());
        Assert.assertEquals("attr1", normalizedAttributes.get("Attr1").getKey());
        Assert.assertEquals("attr1", normalizedAttributes.get("ATTR1").getKey());
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("Attr1", "value2");
        attributes.put("ATTR1", "value3");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(3, normalizedAttributes.size());
        Assert.assertEquals("Attr1", normalizedAttributes.get("Attr1").getKey());
        Assert.assertEquals("ATTR1", normalizedAttributes.get("ATTR1").getKey());
        Assert.assertNull(normalizedAttributes.get("attr1"));
    }
}