package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = settings.normalizeTag("HTML");
        String attrName1 = settings.normalizeAttribute("class");
        String attrName2 = settings.normalizeAttribute("ID");

        assertEquals("html", tagName);
        assertEquals("class", attrName1);
        assertEquals("id", attrName2);
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = settings.normalizeTag("DIV");
        String attrName1 = settings.normalizeAttribute("CLASS");
        String attrName2 = settings.normalizeAttribute("ID");

        assertEquals("DIV", tagName);
        assertEquals("CLASS", attrName1);
        assertEquals("ID", attrName2);
    }

    @Test
    public void testNormalizeTag_lowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = settings.normalizeTag("DIV");

        assertEquals("div", tagName);
    }

    @Test
    public void testNormalizeTag_upperCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("DIV");

        assertEquals("DIV", tagName);
    }

    @Test
    public void testNormalizeAttribute_lowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attrName = settings.normalizeAttribute("CLASS");

        assertEquals("class", attrName);
    }

    @Test
    public void testNormalizeAttribute_upperCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attrName = settings.normalizeAttribute("CLASS");

        assertEquals("CLASS", attrName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("class", "class1");
        attributes.put("id", "ID1");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("class1", normalizedAttributes.get("class"));
        assertEquals("id1", normalizedAttributes.get("id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPreserveCase_bothFalse_NormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("class", "class1");
        attributes.put("id", "ID1");

        settings.normalizeAttributes(attributes);
    }
}