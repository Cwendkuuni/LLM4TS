package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tag = "HTML";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("html", normalizedTag);

        String attribute = "CLASS";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("class", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "class1");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.keyAt(0));
        assertEquals("class1", normalizedAttributes.get(0));
    }

    @Test
    public void testPreserveCaseTag() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tag = "DIV";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("DIV", normalizedTag);

        String attribute = "ID";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("ID", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("ID", "id1");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.keyAt(0));
        assertEquals("id1", normalizedAttributes.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullTag() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tag = null;
        String normalizedTag = settings.normalizeTag(tag);
        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttribute() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attribute = null;
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        fail("Expected NullPointerException");
    }

    @Test
    public void testLowercaseTag() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tag = "DIV";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("div", normalizedTag);
    }

    @Test
    public void testLowercaseAttribute() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String attribute = "ID";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("id", normalizedAttribute);
    }

    @Test
    public void testPreserveCaseAttributeWithValues() {
        ParseSettings settings = ParseSettings.preserveCase;
        String attribute = "ID";
        Attributes attributes = new Attributes();
        attributes.put("ID", "ID1");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.keyAt(0));
        assertEquals("ID1", normalizedAttributes.get(0));
    }

    @Test
    public void testLowercaseAttributes() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("ID", "ID1");
        attributes.put("CLASS", "CLASS1");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.keyAt(0));
        assertEquals("class", normalizedAttributes.keyAt(1));
        assertEquals("id1", normalizedAttributes.get(0));
        assertEquals("class1", normalizedAttributes.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagPreservation() {
        ParseSettings settings = new ParseSettings(false, true);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAttributePreservation() {
        ParseSettings settings = new ParseSettings(true, false);
        fail("Expected IllegalArgumentException");
    }
}