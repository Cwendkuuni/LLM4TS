package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testNewParseSettings() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);

        assertEquals(htmlDefault.preserveTagCase(), false);
        assertEquals(htmlDefault.preserveAttributeCase(), false);

        assertEquals(preserveCase.preserveTagCase(), true);
        assertEquals(preserveCase.preserveAttributeCase(), true);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("tAG"));

        settings = new ParseSettings(false, false);

        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tag"));
        assertEquals("tag", settings.normalizeTag("tAG"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);

        assertEquals("attrName", settings.normalizeAttribute("attrName"));
        assertEquals("ATTR_NAME", settings.normalizeAttribute("ATTR_NAME"));
        assertEquals("AttrName", settings.normalizeAttribute("AttrName"));
        assertEquals("ATTR_name", settings.normalizeAttribute("ATTR_name"));

        settings = new ParseSettings(true, false);

        assertEquals("attrName", settings.normalizeAttribute("attrName"));
        assertEquals("attrname", settings.normalizeAttribute("ATTR_NAME"));
        assertEquals("AttrName", settings.normalizeAttribute("Attrname"));
        assertEquals("ATTR_name", settings.normalizeAttribute("ATTR_name"));

        settings = new ParseSettings(false, false);

        assertEquals("attrname", settings.normalizeAttribute("attrName"));
        assertEquals("attrname", settings.normalizeAttribute("ATTR_NAME"));
        assertEquals("ATTR_name", settings.normalizeAttribute("Attrname"));
        assertEquals("ATTR_name", settings.normalizeAttribute("ATTR_name"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("attrName", "value");
        attributes.put("AttrName", "value");
        attributes.put("Attr name", "value");
        attributes.put("attr_name", "value");

        ParseSettings settings = new ParseSettings(true, true);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(4, normalizedAttributes.size());
        assertEquals("attrName", normalizedAttributes.get("attrName"));
        assertEquals("AttrName", normalizedAttributes.get("AttrName"));
        assertEquals("Attr name", normalizedAttributes.get("Attr name"));
        assertEquals("attr_name", normalizedAttributes.get("attr_name"));

        settings = new ParseSettings(true, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(4, normalizedAttributes.size());
        assertEquals("attrname", normalizedAttributes.get("attrName"));
        assertEquals("AttrName", normalizedAttributes.get("AttrName"));
        assertEquals("Attr name", normalizedAttributes.get("Attr name"));
        assertEquals("attr_name", normalizedAttributes.get("attr_name"));

        settings = new ParseSettings(false, false);
        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(4, normalizedAttributes.size());
        assertEquals("attrname", normalizedAttributes.get("attrName"));
        assertEquals("AttrName", normalizedAttributes.get("AttrName"));
        assertEquals("Attr name", normalizedAttributes.get("Attr name"));
        assertEquals("attr_name", normalizedAttributes.get("attr_name"));
    }

    @Test
    public void testNormalizeTagWithNoChange() {
        ParseSettings settings = new ParseSettings(true, true);

        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeAttributeWithNoChange() {
        ParseSettings settings = new ParseSettings(true, true);

        assertEquals("attrName", settings.normalizeAttribute("attrName"));
        assertEquals("attrName", settings.normalizeAttribute("attrName"));
    }

    @Test
    public void testNormalizeAttributesWithNoChange() {
        Attributes attributes = new Attributes();
        attributes.put("attrName", "value");

        ParseSettings settings = new ParseSettings(true, true);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(1, normalizedAttributes.size());
        assertEquals("attrName", normalizedAttributes.get("attrName"));
    }

    @Test
    public void testNull() {
        ParseSettings settings = new ParseSettings(true, true);
        assertNull(settings.normalizeTag(null));
        assertNull(settings.normalizeAttribute(null));
    }

    @Test
    public void testEmpty() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("", settings.normalizeTag(""));
        assertEquals("", settings.normalizeAttribute(""));
    }
}