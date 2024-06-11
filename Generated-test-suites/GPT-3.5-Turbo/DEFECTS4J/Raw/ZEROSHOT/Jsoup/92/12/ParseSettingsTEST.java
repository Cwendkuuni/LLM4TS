package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        
        String normalizedName = parseSettings.normalizeTag("tag_Name");
        assertEquals("tag_name", normalizedName);
        
        parseSettings = new ParseSettings(true, false);
        normalizedName = parseSettings.normalizeTag("tag_Name");
        assertEquals("tag_Name", normalizedName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        
        String normalizedName = parseSettings.normalizeAttribute("Attr_Name");
        assertEquals("attr_name", normalizedName);
        
        parseSettings = new ParseSettings(false, true);
        normalizedName = parseSettings.normalizeAttribute("Attr_Name");
        assertEquals("Attr_Name", normalizedName);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Attr_1", "value_1");
        attributes.put("Attr_2", "value_2");
        
        ParseSettings parseSettings = new ParseSettings(false, false);

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertFalse(attributes == normalizedAttributes);
        assertEquals("attr_1", normalizedAttributes.get("attr_1"));
        assertEquals("attr_2", normalizedAttributes.get("attr_2"));

        parseSettings = new ParseSettings(false, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertFalse(attributes == normalizedAttributes);
        assertEquals("Attr_1", normalizedAttributes.get("Attr_1"));
        assertEquals("Attr_2", normalizedAttributes.get("Attr_2"));
    }
}