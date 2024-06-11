package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }
    
    @Test
    public void testPreserveTagCase() {
        assertEquals(false, parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        String name = "TAG";
        String expected = "tag";
        
        assertEquals(expected, parseSettings.normalizeTag(name));
    }
    
    @Test
    public void testNormalizeAttribute() {
        String name = "ATTRIBUTE";
        String expected = "attribute";
        
        assertEquals(expected, parseSettings.normalizeAttribute(name));
    }
    
    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "Value1");
        attributes.put("attribute2", "Value2");
        
        Attributes expected = new Attributes();
        expected.put("attribute1", "Value1");
        expected.put("attribute2", "Value2");
        
        assertEquals(expected, parseSettings.normalizeAttributes(attributes));
    }
}