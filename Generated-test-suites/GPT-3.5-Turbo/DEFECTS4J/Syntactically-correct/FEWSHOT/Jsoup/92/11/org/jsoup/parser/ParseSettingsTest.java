package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
        
        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("tag", settings.normalizeTag("tag"));
        
        settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("TAG"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute("attribute"));
        
        settings = new ParseSettings(false, false);
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
        
        settings = new ParseSettings(true, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
    }
}