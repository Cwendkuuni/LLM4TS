package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("HTML");
        String attributeName = settings.normalizeAttribute("class");
        assertTrue(tagName.equalsIgnoreCase("html"));
        assertTrue(attributeName.equalsIgnoreCase("class"));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("DIV");
        assertTrue(tagName.equalsIgnoreCase("DIV"));
    }

    @Test
    public void testPreserveCaseAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = settings.normalizeAttribute("CLASS");
        assertTrue(attributeName.equalsIgnoreCase("CLASS"));
    }

    @Test
    public void testPreserveCaseBoth() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = settings.normalizeTag("SCRIPT");
        String attributeName = settings.normalizeAttribute("type");
        assertTrue(tagName.equalsIgnoreCase("SCRIPT"));
        assertTrue(attributeName.equalsIgnoreCase("type"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArguments() {
        new ParseSettings(true, "invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidBooleanAttribute() {
        new ParseSettings(false, "invalid");
    }

    @Test
    public void testNormalizeAttributes() {
        String input = "class=\"foo bar Baz\"; id=\"ID_1\";";
        Attributes attributes = new Attributes(input);
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalized = settings.normalizeAttributes(attributes);
        String output = normalized.toString();
        String expected = "class=foo bar Baz; id=ID_1;";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testNormalizeAttributesPreserveAttributeCase() {
        String input = "class=\"foo bar Baz\"; id=\"ID_1\";";
        Attributes attributes = new Attributes(input);
        ParseSettings settings = new ParseSettings(false, true);
        Attributes normalized = settings.normalizeAttributes(attributes);
        String output = normalized.toString();
        String expected = "class=\"foo bar Baz\"; id=\"ID_1\";";
        assertTrue(output.equals(expected));
    }
}