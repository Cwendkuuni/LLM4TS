package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
        
        settings = new ParseSettings(false, true);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("tag", settings.normalizeTag("TaG"));
        
        settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("TaG"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("attribute", settings.normalizeAttribute("Attribute"));
        
        settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute("Attribute"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value1", normalizedAttributes.get("attr1"));
        assertEquals("value2", normalizedAttributes.get("attr2"));
        
        settings = new ParseSettings(false, true);
        attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value1", normalizedAttributes.get("attr1"));
        assertEquals("value2", normalizedAttributes.get("attr2"));
    }
}