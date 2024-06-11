package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testPreserveCase_TagName() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TAG", settings.normalizeTag("TAG"));
        assertEquals("TAG", settings.normalizeTag("tAG"));
        assertEquals("TAG", settings.normalizeTag("TAG")); // already lower-case
        assertEquals("TAG", settings.normalizeTag("TAG")); // already upper-case
    }

    @Test
    public void testPreserveCase_TagName_Lowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("tAG"));
        assertEquals("tag", settings.normalizeTag("TAG"));
        assertEquals("tag", settings.normalizeTag("TAG"));
    }

    @Test
    public void testPreserveCase_AttributeName() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("ATTR", settings.normalizeAttribute("ATTR"));
        assertEquals("ATTR", settings.normalizeAttribute("aTRR"));
        assertEquals("ATTR", settings.normalizeAttribute("ATTR")); // already lower-case
        assertEquals("ATTR", settings.normalizeAttribute("ATTR")); // already upper-case
    }

    @Test
    public void testPreserveCase_AttributeName_Lowercase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("attr", settings.normalizeAttribute("ATTR"));
        assertEquals("attr", settings.normalizeAttribute("aTRR"));
        assertEquals("attr", settings.normalizeAttribute("ATTR"));
        assertEquals("attr", settings.normalizeAttribute("ATTR"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "value1");
        attributes.put("Attr2", "value2");
        attributes.put("Attr3", "value3");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("attr1", normalizedAttributes.get("Attr1"));
        assertEquals("attr2", normalizedAttributes.get("Attr2"));
        assertEquals("attr3", normalizedAttributes.get("Attr3"));
    }

    @Test
    public void testNormalizeAttributes_PreserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "value1");
        attributes.put("Attr2", "value2");
        attributes.put("Attr3", "value3");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes);
        assertEquals("Attr1", normalizedAttributes.get("Attr1"));
        assertEquals("Attr2", normalizedAttributes.get("Attr2"));
        assertEquals("Attr3", normalizedAttributes.get("Attr3"));
    }
}