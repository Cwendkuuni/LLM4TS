package org.jsoup.parser;

import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings1 = new ParseSettings(true, false);
        ParseSettings settings2 = new ParseSettings(false, false);
        
        assertTrue(settings1.preserveTagCase());
        assertFalse(settings2.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings1 = new ParseSettings(false, true);
        ParseSettings settings2 = new ParseSettings(false, false);
        
        assertTrue(settings1.preserveAttributeCase());
        assertFalse(settings2.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings1 = new ParseSettings(true, false);
        ParseSettings settings2 = new ParseSettings(false, false);
        
        assertEquals("div", settings1.normalizeTag("DIV"));
        assertEquals("div", settings2.normalizeTag("DIV"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings1 = new ParseSettings(false, true);
        ParseSettings settings2 = new ParseSettings(false, false);
        
        assertEquals("class", settings1.normalizeAttribute("CLASS"));
        assertEquals("class", settings2.normalizeAttribute("CLASS"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings1 = new ParseSettings(false, true);
        ParseSettings settings2 = new ParseSettings(false, false);
        
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "example");
        
        assertEquals(attributes, settings1.normalizeAttributes(attributes));
        assertEquals(attributes, settings2.normalizeAttributes(attributes));
    }
}