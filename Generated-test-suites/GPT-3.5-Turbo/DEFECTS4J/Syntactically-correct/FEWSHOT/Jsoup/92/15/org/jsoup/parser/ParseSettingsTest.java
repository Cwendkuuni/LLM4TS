package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("html", settings.normalizeTag("html"));
        assertEquals("body", settings.normalizeTag("BODY"));

        settings = new ParseSettings(false, false);
        assertEquals("html", settings.normalizeTag("html"));
        assertEquals("body", settings.normalizeTag("BODY"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("id", settings.normalizeAttribute("ID"));

        settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("id", settings.normalizeAttribute("ID"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "foo");

        ParseSettings settings = new ParseSettings(false, false);
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("class", attributes.asList().get(0).getKey());
        
        settings = new ParseSettings(false, true);
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("class", attributes.asList().get(0).getKey());
    }
}