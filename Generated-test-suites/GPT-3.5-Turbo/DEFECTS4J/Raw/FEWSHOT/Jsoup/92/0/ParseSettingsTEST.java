package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
        
        parseSettings = new ParseSettings(false, false);
        assertEquals(false, parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
        
        parseSettings = new ParseSettings(false, false);
        assertEquals(false, parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("div", parseSettings.normalizeTag("div"));
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("div", parseSettings.normalizeTag(" dIv "));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("class", parseSettings.normalizeAttribute("class"));
        assertEquals("class", parseSettings.normalizeAttribute("CLASS"));
        assertEquals("class", parseSettings.normalizeAttribute(" ClAsS "));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("class", "testClass");
        attributes.put("ID", "testId");
        attributes.put("hReF", "testHref");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals(3, normalizedAttributes.size());
        assertEquals("testClass", normalizedAttributes.get("class"));
        assertEquals("testId", normalizedAttributes.get("id"));
        assertEquals("testHref", normalizedAttributes.get("href"));
    }
}