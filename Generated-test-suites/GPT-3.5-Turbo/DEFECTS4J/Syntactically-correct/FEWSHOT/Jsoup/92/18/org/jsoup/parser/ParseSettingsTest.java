package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        assertEquals("HTML", settings.normalizeTag("HTML"));
        assertEquals("Div", settings.normalizeTag("Div"));
        assertEquals("p", settings.normalizeTag("p"));
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertEquals("html", settings.normalizeTag("HTML"));
        assertEquals("div", settings.normalizeTag("Div"));
        assertEquals("p", settings.normalizeTag("p"));
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("Id", settings.normalizeAttribute("Id"));
        assertEquals("data-src", settings.normalizeAttribute("data-src"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("id", settings.normalizeAttribute("Id"));
        assertEquals("data-src", settings.normalizeAttribute("data-src"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("ID", "123");
        attributes.put("Data-Src", "http://example.com");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getKey(0));
        assertEquals("example", normalizedAttributes.getValue(0));
        assertEquals("id", normalizedAttributes.getKey(1));
        assertEquals("123", normalizedAttributes.getValue(1));
        assertEquals("data-src", normalizedAttributes.getKey(2));
        assertEquals("http://example.com", normalizedAttributes.getValue(2));
    }
}