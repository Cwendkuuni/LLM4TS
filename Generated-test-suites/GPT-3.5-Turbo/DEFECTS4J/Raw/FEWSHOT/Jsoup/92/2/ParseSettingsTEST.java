package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        
        assertTrue(settings.preserveTagCase());
        
        assertEquals("HTML", settings.normalizeTag("HTML"));
        assertEquals("html", settings.normalizeTag("html"));
        assertEquals("hTmL", settings.normalizeTag("hTmL"));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        
        assertFalse(settings.preserveTagCase());
        
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
        assertEquals("cLaSs", settings.normalizeAttribute("cLaSs"));
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertFalse(settings.preserveTagCase());
        
        assertEquals("html", settings.normalizeTag("HTML"));
        assertEquals("html", settings.normalizeTag("html"));
        assertEquals("html", settings.normalizeTag("hTmL"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertFalse(settings.preserveAttributeCase());
        
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("class", settings.normalizeAttribute("cLaSs"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        
        assertFalse(settings.preserveAttributeCase());
        
        Attributes attributes = new Attributes();
        
        attributes.put("class", "value");
        attributes.put("ID", "value");
        attributes.put("StYlE", "value");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertEquals("class", normalizedAttributes.keySet().toArray()[0]);
        assertEquals("id", normalizedAttributes.keySet().toArray()[1]);
        assertEquals("style", normalizedAttributes.keySet().toArray()[2]);
    }
}