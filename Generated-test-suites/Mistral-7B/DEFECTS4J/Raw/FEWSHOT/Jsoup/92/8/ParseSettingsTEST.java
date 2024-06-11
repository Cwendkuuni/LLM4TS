package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsInstance() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertTrue(htmlDefault.preserveTagCase() == false);
        assertTrue(htmlDefault.preserveAttributeCase() == false);
        assertTrue(preserveCase.preserveTagCase() == true);
        assertTrue(preserveCase.preserveAttributeCase() == true);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, true);

        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("tAG"));
        assertEquals("TAG", settings.normalizeTag("TAG")); // no change if already lowercase

        settings = new ParseSettings(true, false);

        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("tAG"));
        assertEquals("TAG", settings.normalizeTag("tag"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("attrName", settings.normalizeAttribute("attrName"));
        assertEquals("attrName", settings.normalizeAttribute("ATTR_NAME"));
        assertEquals("attrName", settings.normalizeAttribute("attr name"));

        settings = new ParseSettings(false, false);

        assertEquals("attrName", settings.normalizeAttribute("attrName"));
        assertEquals("attrName", settings.normalizeAttribute("ATTR_NAME"));
        assertEquals("attrName", settings.normalizeAttribute("attr name"));
        assertEquals("attrName", settings.normalizeAttribute("attrName")); // no change if already lowercase
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("attrName", "value");
        attributes.put("attrName2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("attrName", normalizedAttributes.get("attrName").getValue());
        assertEquals("attrName2", normalizedAttributes.get("attrName2").getValue());

        settings = new ParseSettings(false, false);

        attributes = new Attributes();
        attributes.put("attrName", "value");
        attributes.put("attrName2", "value2");

        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("attrName", normalizedAttributes.get("attrName").getValue());
        assertEquals("attrName2", normalizedAttributes.get("attrName2").getValue());
        assertEquals("attrName", normalizedAttributes.get("AttrName").getValue()); // unchanged if not preserved
        assertNull(normalizedAttributes.get("ATTR_NAME")); // unchanged if not preserved
    }
}