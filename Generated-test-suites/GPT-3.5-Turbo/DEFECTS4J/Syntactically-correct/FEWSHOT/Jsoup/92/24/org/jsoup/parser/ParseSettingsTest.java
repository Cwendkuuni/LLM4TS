package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

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
        ParseSettings settings = new ParseSettings(true, true);
        
        String tag = settings.normalizeTag("Tag");
        assertEquals("Tag", tag);
        
        settings = new ParseSettings(false, true);
        
        tag = settings.normalizeTag("Tag");
        assertEquals("tag", tag);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        
        attributes = settings.normalizeAttributes(attributes);
        
        assertEquals("Value1", attributes.getIgnoreCase("Attr1"));
        
        settings = new ParseSettings(false, true);
        
        attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        
        attributes = settings.normalizeAttributes(attributes);
        
        assertEquals("Value1", attributes.getIgnoreCase("attr1"));
    }
}