package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultSettings() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertTrue(settings.preserveTagCase() == false);
        assertTrue(settings.preserveAttributeCase() == false);
    }

    @Test
    public void testPreserveCaseSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveTagCase() == true);
        assertTrue(settings.preserveAttributeCase() == true);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        assertEquals("DIV", settings.normalizeTag("DIV"));
        assertEquals("DIV", settings.normalizeTag("DIv")); // Case insensitive check
        assertEquals("DIV", settings.normalizeTag("div")); // Lowercase check

        ParseSettings htmlDefaultSettings = ParseSettings.htmlDefault;
        assertEquals("div", htmlDefaultSettings.normalizeTag("DIV")); // Lowercase check
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("class", settings.normalizeAttribute("class")); // Lowercase check
        assertEquals("Class", settings.normalizeAttribute("CLASS")); // Case insensitive check

        ParseSettings htmlDefaultSettings = ParseSettings.htmlDefault;
        assertEquals("class", htmlDefaultSettings.normalizeAttribute("CLASS")); // Lowercase check
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("class", "one two three");
        attributes.put("id", "some_id");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("one two three", normalizedAttributes.get("class"));
        assertEquals("some_id", normalizedAttributes.get("id"));

        ParseSettings htmlDefaultSettings = ParseSettings.htmlDefault;
        Attributes htmlDefaultNormalizedAttributes = htmlDefaultSettings.normalizeAttributes(attributes);

        assertEquals("one two three", htmlDefaultNormalizedAttributes.get("class")); // Lowercase check
        assertEquals("some_id", htmlDefaultNormalizedAttributes.get("id"));
    }
}