package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagNameLower = settings.normalizeTag("HTML");
        String tagNamePreserved = settings.normalizeTag("HTML");
        String attrNameLower = settings.normalizeAttribute("class");
        String attrNamePreserved = settings.normalizeAttribute("CLASS");

        assertEquals("html", tagNameLower);
        assertEquals("HTML", tagNamePreserved);
        assertEquals("class", attrNameLower);
        assertEquals("CLASS", attrNamePreserved);
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagNameLower = settings.normalizeTag("HTML");
        String tagNamePreserved = settings.normalizeTag("HTML");
        String attrNameLower = settings.normalizeAttribute("class");
        String attrNamePreserved = settings.normalizeAttribute("CLASS");

        assertNotEquals("html", tagNamePreserved);
        assertNotEquals("HTML", tagNameLower);
        assertNotEquals("class", attrNamePreserved);
        assertNotEquals("CLASS", attrNameLower);

        assertEquals("HTML", tagNamePreserved);
        assertEquals("html", tagNameLower);
        assertEquals("CLASS", attrNamePreserved);
        assertEquals("class", attrNameLower);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeTagNullName() {
        ParseSettings settings = new ParseSettings(true, true);
        String name = null;
        settings.normalizeTag(name);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributeNullName() {
        ParseSettings settings = new ParseSettings(true, true);
        String name = null;
        settings.normalizeAttribute(name);
    }

    @Test
    public void testNormalizeAttributesLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2 class3");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("class1", normalizedAttributes.get("class"));
        assertEquals("class2", normalizedAttributes.get("class2"));
        assertEquals("class3", normalizedAttributes.get("class3"));
    }

    @Test
    public void testNormalizeAttributesCasePreserved() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("class", "class1 class2 class3");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("class1", normalizedAttributes.get("class"));
        assertEquals("class2", normalizedAttributes.get("class2"));
        assertEquals("class3", normalizedAttributes.get("class3"));
    }
}