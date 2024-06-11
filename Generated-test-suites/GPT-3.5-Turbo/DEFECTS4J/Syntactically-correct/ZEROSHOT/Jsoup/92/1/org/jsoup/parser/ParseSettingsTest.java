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
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String normalizedTag = settings.normalizeTag("DIV");
        Assert.assertEquals("div", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String normalizedAttribute = settings.normalizeAttribute("HREF");
        Assert.assertEquals("href", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals("http://example.com", normalizedAttributes.get("href"));
    }
    
}