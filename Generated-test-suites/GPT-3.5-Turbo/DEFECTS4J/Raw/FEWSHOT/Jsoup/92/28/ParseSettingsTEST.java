package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveAttributeCase());
        
        settings = new ParseSettings(true, false);
        assertFalse(settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String input = "TeStTaG";
        assertEquals("TeStTaG", settings.normalizeTag(input));
        
        settings = new ParseSettings(false, false);
        assertEquals("testtag", settings.normalizeTag(input));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        String input = "TeStAttr";
        assertEquals("TeStAttr", settings.normalizeAttribute(input));
        
        settings = new ParseSettings(true, false);
        assertEquals("testattr", settings.normalizeAttribute(input));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("Value1", normalizedAttributes.get("Attr1"));
        assertEquals("Value2", normalizedAttributes.get("Attr2"));
        
        settings = new ParseSettings(true, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("Value1", normalizedAttributes.get("attr1"));
        assertEquals("Value2", normalizedAttributes.get("attr2"));
    }
}