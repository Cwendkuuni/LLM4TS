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
        htmlDefault = new ParseSettings(false, false);
        preserveCase = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(htmlDefault.preserveAttributeCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("title", settings.normalizeTag("title"));
        assertEquals("a", settings.normalizeTag("A"));
        assertEquals("div", settings.normalizeTag("Div"));
        
        settings = new ParseSettings(true, true);
        assertEquals("title", settings.normalizeTag("title"));
        assertEquals("A", settings.normalizeTag("A"));
        assertEquals("Div", settings.normalizeTag("Div"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("name", settings.normalizeAttribute("name"));
        assertEquals("type", settings.normalizeAttribute("TYPE"));
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        
        settings = new ParseSettings(true, true);
        assertEquals("name", settings.normalizeAttribute("name"));
        assertEquals("TYPE", settings.normalizeAttribute("TYPE"));
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("name", "value");
        attributes.put("TYPE", "text");
        attributes.put("CLASS", "container");
        
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("name", attributes.getKey("name"));
        assertEquals("type", attributes.getKey("type"));
        assertEquals("class", attributes.getKey("class"));
        
        settings = new ParseSettings(true, true);
        attributes = new Attributes();
        attributes.put("name", "value");
        attributes.put("TYPE", "text");
        attributes.put("CLASS", "container");
        
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("name", attributes.getKey("name"));
        assertEquals("TYPE", attributes.getKey("TYPE"));
        assertEquals("CLASS", attributes.getKey("CLASS"));
    }
}