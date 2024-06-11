package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        // Test with normalizing tag
        assertEquals("html", settings.normalizeTag("HTML"));
        assertEquals("head", settings.normalizeTag("head"));
        assertEquals("body", settings.normalizeTag("Body"));
        assertEquals("div", settings.normalizeTag("DIv"));
        
        // Test without normalizing tag
        assertEquals("HTML", settings.normalizeTag("HTML"));
        assertEquals("head", settings.normalizeTag("head"));
        assertEquals("Body", settings.normalizeTag("Body"));
        assertEquals("DIv", settings.normalizeTag("DIv"));
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        // Test with normalizing attribute
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("id", settings.normalizeAttribute("id"));
        assertEquals("href", settings.normalizeAttribute("HreF"));
        assertEquals("src", settings.normalizeAttribute("sRC"));
        
        // Test without normalizing attribute
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
        assertEquals("id", settings.normalizeAttribute("id"));
        assertEquals("HreF", settings.normalizeAttribute("HreF"));
        assertEquals("sRC", settings.normalizeAttribute("sRC"));
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        
        assertEquals("html", settings.normalizeTag("HTML"));
        assertEquals("head", settings.normalizeTag("head"));
        assertEquals("body", settings.normalizeTag("Body"));
        assertEquals("div", settings.normalizeTag("DIv"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("id", settings.normalizeAttribute("id"));
        assertEquals("href", settings.normalizeAttribute("HreF"));
        assertEquals("src", settings.normalizeAttribute("sRC"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value1");
        attributes.put("id", "value2");
        attributes.put("HreF", "value3");
        attributes.put("sRC", "value4");
        
        assertEquals(attributes, settings.normalizeAttributes(attributes));
    }
}