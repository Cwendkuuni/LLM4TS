package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testCreateParseSettingsObjects() {
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

        assertEquals("DIV", settings.normalizeTag("DIV"));
        assertEquals("DIV", settings.normalizeTag("div"));

        assertEquals("DIV", settings.normalizeTag("DIv"));
        assertEquals("DIV", settings.normalizeTag("DIv")); // case insensitive comparison

        assertEquals("DIV", settings.normalizeTag("DIV ")); // leading whitespace
        assertEquals("DIV", settings.normalizeTag("DIV \t ")); // leading whitespace with tab

        // empty string
        assertEquals("", settings.normalizeTag(""));

        // null
        assertNull(settings.normalizeTag(null));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("class", settings.normalizeAttribute("CLASS"));

        assertEquals("class", settings.normalizeAttribute("class\t ")); // leading whitespace
        assertEquals("class", settings.normalizeAttribute("class \t ")); // leading whitespace with tab

        // empty string
        assertEquals("", settings.normalizeAttribute(""));

        // null
        assertNull(settings.normalizeAttribute(null));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("class", "foo");
        attributes.put("class2", "bar");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("foo", normalizedAttributes.get("class"));
        assertEquals("bar", normalizedAttributes.get("class2"));

        // empty attributes
        attributes = new Attributes();
        normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(0, normalizedAttributes.size());

        // null attributes
        assertNull(settings.normalizeAttributes(null));
    }
}