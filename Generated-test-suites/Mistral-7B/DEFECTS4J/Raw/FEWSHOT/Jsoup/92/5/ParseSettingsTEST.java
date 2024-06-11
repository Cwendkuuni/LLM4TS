package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("TAG", settings.normalizeTag("TAGS"));
        assertEquals("tag", settings.normalizeTag("tagS"));
        assertNull(settings.normalizeTag(null));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
        assertEquals("ATTRIBUTE", settings.normalizeAttribute("attribute"));
        assertEquals("attribute", settings.normalizeAttribute("attribute: test"));
        assertEquals("test", settings.normalizeAttribute("attribute: test"));
        assertNull(settings.normalizeAttribute(null));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attribute1", normalizedAttributes.get("attribute1"));
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("attribute2", normalizedAttributes.get("attribute2"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));

        attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");
        attributes.put("attribute3", null);

        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attribute1", normalizedAttributes.get("attribute1"));
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("attribute2", normalizedAttributes.get("attribute2"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
        assertNull(normalizedAttributes.get("attribute3"));
    }

    @Test
    public void testNormalizeAttributesWithLowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attribute1", normalizedAttributes.get("attribute1"));
        assertEquals("value1", normalizedAttributes.get("attribute1"));
        assertEquals("attribute2", normalizedAttributes.get("attribute2"));
        assertEquals("value2", normalizedAttributes.get("attribute2"));
    }
}