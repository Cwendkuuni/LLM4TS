package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsConstructor() {
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

        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertEquals("tag", htmlDefault.normalizeTag("TAG"));
        assertEquals("TAG", htmlDefault.normalizeTag("tag"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("attr", settings.normalizeAttribute("Attr"));
        assertEquals("Attr", settings.normalizeAttribute("attr"));
        assertEquals("attr", settings.normalizeAttribute("Attr"));

        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertEquals("attr", htmlDefault.normalizeAttribute("Attr"));
        assertEquals("Attr", htmlDefault.normalizeAttribute("attr"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attr-one", "value-one");
        attributes.put("ATTR-TWO", "value-two");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr-one", normalizedAttributes.get("attr-one"));
        assertEquals("attr-two", normalizedAttributes.get("attr-two"));

        attributes = new Attributes();
        attributes.put("attr-one", "value-one");
        attributes.put("ATTR-TWO", "value-two");

        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        normalizedAttributes = htmlDefault.normalizeAttributes(attributes);

        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr-one", normalizedAttributes.get("attr-one"));
        assertEquals("attr-two", normalizedAttributes.get("attr-two"));
    }

    @Test
    public void testNullNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = null;
        assertNull(settings.normalizeAttributes(attributes));

        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertNull(htmlDefault.normalizeAttributes(attributes));
    }

    @Test
    public void testPreserveCaseValues() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("attr-one", "value-one");
        attributes.put("ATTR-TWO", "value-two");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr-one", normalizedAttributes.get("attr-one"));
        assertEquals("ATTR-TWO", normalizedAttributes.get("ATTR-TWO"));

        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        normalizedAttributes = htmlDefault.normalizeAttributes(attributes);

        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr-one", normalizedAttributes.get("attr-one"));
        assertEquals("ATTR-TWO", normalizedAttributes.get("ATTR-TWO"));
    }
}