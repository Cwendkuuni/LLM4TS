package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        Assert.assertEquals("tag", tagNameLower);

        String attrNameLower = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("attr", attrNameLower);

        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("attr", normalizedAttributes.get("attr").getValue());
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagNamePreserved = settings.normalizeTag("TAG");
        Assert.assertEquals("TAG", tagNamePreserved);

        String attrNameLower = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("attr", attrNameLower);

        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("ATTR", normalizedAttributes.get("ATTR").getValue());
    }

    @Test
    public void testPreserveCaseBoth() {
        ParseSettings settings = new ParseSettings(true, true);
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagNamePreserved = settings.normalizeTag("TAG");
        Assert.assertEquals("TAG", tagNamePreserved);

        String attrNamePreserved = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("ATTR", attrNamePreserved);

        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("ATTR", normalizedAttributes.get("ATTR").getValue());
    }

    @Test
    public void testLowercaseTag() {
        ParseSettings settings = new ParseSettings(false, false);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        Assert.assertEquals("tag", tagNameLower);

        String attrNameLower = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("attr", attrNameLower);

        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("attr", normalizedAttributes.get("attr").getValue());
    }

    @Test
    public void testLowercaseAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tagNamePreserved = settings.normalizeTag("TAG");
        Assert.assertEquals("tag", tagNamePreserved);

        String attrNameLower = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("attr", attrNameLower);

        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("attr", normalizedAttributes.get("attr").getValue());
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tagNameLower = settings.normalizeTag("TAG");
        Assert.assertEquals("tag", tagNameLower);

        String attrNamePreserved = settings.normalizeAttribute("ATTR");
        Assert.assertEquals("ATTR", attrNamePreserved);

        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNotNull(normalizedAttributes);
        Assert.assertEquals("ATTR", normalizedAttributes.get("ATTR").getValue());
    }
}