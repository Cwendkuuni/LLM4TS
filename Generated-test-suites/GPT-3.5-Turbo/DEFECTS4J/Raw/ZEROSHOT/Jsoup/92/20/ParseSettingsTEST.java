package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Assert.assertTrue(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Assert.assertTrue(parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String name = parseSettings.normalizeTag("div");
        Assert.assertEquals("div", name);
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String name = parseSettings.normalizeAttribute("class");
        Assert.assertEquals("class", name);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertEquals("test", normalizedAttributes.get("class"));
    }
}