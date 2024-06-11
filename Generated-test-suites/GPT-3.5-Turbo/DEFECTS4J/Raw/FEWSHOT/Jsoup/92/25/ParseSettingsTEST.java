package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("div", settings.normalizeTag("div"));
        assertEquals("DIV", settings.normalizeTag("DIV"));
        assertEquals("Div", settings.normalizeTag("Div"));
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
        assertEquals("Class", settings.normalizeAttribute("Class"));
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("div", settings.normalizeTag("DIV"));
        assertEquals("div", settings.normalizeTag("Div"));
        assertEquals("div", settings.normalizeTag("div"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("class", settings.normalizeAttribute("Class"));
        assertEquals("class", settings.normalizeAttribute("class"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "test");
        attributes.put("id", "test");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getKey(0));
        assertEquals("id", normalizedAttributes.getKey(1));
    }
}