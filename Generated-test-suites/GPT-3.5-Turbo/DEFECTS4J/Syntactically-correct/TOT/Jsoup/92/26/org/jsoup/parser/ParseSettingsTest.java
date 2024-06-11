package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Assert.assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Assert.assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_lowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "div";
        String normalizedTag = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizedTag);
    }

    @Test
    public void testNormalizeTag_preserveCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "div";
        String normalizedTag = settings.normalizeTag(tagName);
        Assert.assertEquals("div", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute_lowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "HREF";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("href", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttribute_preserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "HREF";
        String normalizedAttribute = settings.normalizeAttribute(attributeName);
        Assert.assertEquals("HREF", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes_preserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://example.com");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        Assert.assertEquals("https://example.com", normalizedAttributes.getValue("HREF"));
    }

    @Test
    public void testNormalizeAttributes_lowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://example.com");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        Assert.assertEquals("https://example.com", normalizedAttributes.getValue("href"));
    }
}