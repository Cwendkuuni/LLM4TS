package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("Tag", settings.normalizeTag("Tag"));
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("tAg", settings.normalizeTag("tAg"));
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        assertEquals("attribute", settings.normalizeAttribute("attribute"));
        assertEquals("Attribute", settings.normalizeAttribute("Attribute"));
        assertEquals("ATTRIBUTE", settings.normalizeAttribute("ATTRIBUTE"));
        assertEquals("aTtRiBuTe", settings.normalizeAttribute("aTtRiBuTe"));
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("tag", settings.normalizeTag("Tag"));
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tAg"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertEquals("attribute", settings.normalizeAttribute("attribute"));
        assertEquals("attribute", settings.normalizeAttribute("Attribute"));
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
        assertEquals("attribute", settings.normalizeAttribute("aTtRiBuTe"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
    }
}