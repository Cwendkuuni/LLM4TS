package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.jsoup.internal.Normalizer.lowerCase;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_preserveCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeTag_noPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tag"));
    }

    @Test
    public void testNormalizeTag_null() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeTag(null));
    }

    @Test
    public void testNormalizeAttribute_preserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("ATTR", settings.normalizeAttribute("ATTR"));
        assertEquals("ATTR", settings.normalizeAttribute("ATTR"));
    }

    @Test
    public void testNormalizeAttribute_noPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("attr", settings.normalizeAttribute("ATTR"));
        assertEquals("attr", settings.normalizeAttribute("attr"));
    }

    @Test
    public void testNormalizeAttribute_null() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeAttribute(null));
    }

    @Test
    public void testNormalizeAttributes_noPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr1", normalizedAttributes.get("attr1"));
        assertEquals("value1", normalizedAttributes.get("attr1"));
        assertEquals("attr2", normalizedAttributes.get("attr2"));
        assertEquals("value2", normalizedAttributes.get("attr2"));
    }

    @Test
    public void testNormalizeAttributes_preserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("Attr1", normalizedAttributes.get("attr1"));
        assertEquals("Value1", normalizedAttributes.get("attr1"));
        assertEquals("Attr2", normalizedAttributes.get("attr2"));
        assertEquals("Value2", normalizedAttributes.get("attr2"));
    }

    @Test
    public void testNormalizeAttributes_null() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeAttributes(null));
    }
}