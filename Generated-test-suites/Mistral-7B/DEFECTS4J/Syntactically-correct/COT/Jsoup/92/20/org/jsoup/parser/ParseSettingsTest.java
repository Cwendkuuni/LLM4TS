package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testParseSettingsConstructor() {
        ParseSettings settings1 = new ParseSettings(false, false);
        ParseSettings settings2 = new ParseSettings(true, true);
        assertNotNull(settings1);
        assertNotNull(settings2);
    }

    @Test
    public void testHtmlDefaultPreserveCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCasePreserveTagAttribute() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeTagNullName() {
        ParseSettings settings = new ParseSettings(true, true);
        settings.normalizeTag(null);
    }

    @Test
    public void testNormalizeTagUpperCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    @Test
    public void testNormalizeTagLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);
    }

    @Test
    public void testNormalizeAttributeUpperCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String attrName = "ID";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals(attrName, normalizedAttrName);
    }

    @Test
    public void testNormalizeAttributeLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attrName = "ID";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals("id", normalizedAttrName);
    }

    @Test
    public void testNormalizeAttributesNullAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNotNull(settings.normalizeAttributes(null));
    }

    @Test
    public void testNormalizeAttributesEmptyAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(0, normalizedAttributes.size());
    }

    @Test
    public void testNormalizeAttributesCasePreserved() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "value1");
        attributes.put("CLASS", "value2");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("ID", normalizedAttributes.get("ID"));
        assertEquals("CLASS", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testNormalizeAttributesCaseNotPreserved() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "value1");
        attributes.put("CLASS", "value2");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("id", normalizedAttributes.get("id"));
        assertEquals("class", normalizedAttributes.get("class"));
    }
}