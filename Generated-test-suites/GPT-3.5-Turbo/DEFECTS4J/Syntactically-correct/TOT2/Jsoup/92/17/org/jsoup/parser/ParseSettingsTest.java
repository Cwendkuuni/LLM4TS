package org.jsoup.parser;

import org.jsoup.nodes.Attributes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    private ParseSettings htmlDefault;
    private ParseSettings preserveCase;

    @Before
    public void setUp() {
        htmlDefault = ParseSettings.htmlDefault;
        preserveCase = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(htmlDefault.preserveAttributeCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("img", settings.normalizeTag("IMG"));
        assertEquals("b", settings.normalizeTag(" b "));
        
        settings = new ParseSettings(true, true);
        assertEquals("IMG", settings.normalizeTag("IMG"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("src", settings.normalizeAttribute("SRC"));
        assertEquals("href", settings.normalizeAttribute(" HREF "));
        
        settings = new ParseSettings(true, true);
        assertEquals("SRC", settings.normalizeAttribute("SRC"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("src", "example.jpg");
        attributes.put("HREF", "http://example.com");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("example.jpg", normalizedAttributes.getIgnoreCase("src"));
        assertEquals("http://example.com", normalizedAttributes.getIgnoreCase("HREF"));
        
        settings = new ParseSettings(true, true);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("example.jpg", normalizedAttributes.getIgnoreCase("src"));
        assertEquals("http://example.com", normalizedAttributes.getIgnoreCase("HREF"));
    }
}