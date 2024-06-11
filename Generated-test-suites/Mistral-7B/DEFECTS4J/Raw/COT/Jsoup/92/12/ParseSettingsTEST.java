package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    /**
     * Test constructor with tag and attribute case preservation enabled.
     */
    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
    }

    /**
     * Test constructor with tag case preservation disabled and attribute case preservation enabled.
     */
    @Test
    public void testParseSettingsPreserveTagCaseFalseAndAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(false, true);
        assertFalse(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
    }

    /**
     * Test constructor with tag case preservation enabled and attribute case preservation disabled.
     */
    @Test
    public void testParseSettingsPreserveTagCaseTrueAndAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
        assertFalse(settings.preserveAttributeCase());
    }

    /**
     * Test constructor with tag and attribute case preservation disabled.
     */
    @Test
    public void testParseSettingsDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertFalse(settings.preserveTagCase());
        assertFalse(settings.preserveAttributeCase());
    }

    /**
     * Test normalizeTag method with tag name in lower case.
     */
    @Test
    public void testNormalizeTagLowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String result = settings.normalizeTag("div");
        assertEquals("div", result);
    }

    /**
     * Test normalizeTag method with tag name in upper case.
     */
    @Test
    public void testNormalizeTagUpperCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String result = settings.normalizeTag("DIV");
        assertEquals("DIV", result);
    }

    /**
     * Test normalizeTag method with tag name having mixed case.
     */
    @Test
    public void testNormalizeTagMixedCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String result = settings.normalizeTag("DIv");
        assertEquals("DIv", result);
    }

    /**
     * Test normalizeTag method with empty tag name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeTagEmpty() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String result = settings.normalizeTag("");
    }

    /**
     * Test normalizeAttribute method with attribute name in lower case.
     */
    @Test
    public void testNormalizeAttributeLowerCase() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String result = settings.normalizeAttribute("class");
        assertEquals("class", result);
    }

    /**
     * Test normalizeAttribute method with attribute name in upper case.
     */
    @Test
    public void testNormalizeAttributeUpperCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String result = settings.normalizeAttribute("CLASS");
        assertEquals("CLASS", result);
    }

    /**
     * Test normalizeAttribute method with attribute name having mixed case.
     */
    @Test
    public void testNormalizeAttributeMixedCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String result = settings.normalizeAttribute("ClaSs");
        assertEquals("ClaSs", result);
    }

    /**
     * Test normalizeAttribute method with empty attribute name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAttributeEmpty() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String result = settings.normalizeAttribute("");
    }

    /**
     * Test normalizeAttributes method with attributes having lower case names.
     */
    @Test
    public void testNormalizeAttributesLowerCase() {
        String input = "class=\"foo\" id=\"bar\" data-test=\"qux\"";
        Attributes attributes = new Attributes(input);
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes result = settings.normalizeAttributes(attributes);
        assertEquals("class=\"foo\" id=\"bar\" data-test=\"qux\"", result.toString());
    }

    /**
     * Test normalizeAttributes method with attributes having upper case names.
     */
    @Test
    public void testNormalizeAttributesUpperCase() {
        String input = "CLASS=\"foo\" ID=\"bar\" DATA-TEST=\"qux\"";
        Attributes attributes = new Attributes(input);
        ParseSettings settings = new ParseSettings(true, true);
        Attributes result = settings.normalizeAttributes(attributes);
        assertEquals("CLASS=\"foo\" ID=\"bar\" DATA-TEST=\"qux\"", result.toString());
    }

    /**
     * Test normalizeAttributes method with attributes having mixed case names.
     */
    @Test
    public void testNormalizeAttributesMixedCase() {
        String input = "class=\"foo\" ID=\"bar\" data-test=\"qux\"";
        Attributes attributes = new Attributes(input);
        ParseSettings settings = new ParseSettings(true, true);
        Attributes result = settings.normalizeAttributes(attributes);
        assertEquals("class=\"foo\" ID=\"bar\" data-test=\"qux\"", result.toString());
    }

    /**
     * Test normalizeAttributes method with empty attributes string.
     */
    @Test
    public void testNormalizeAttributesEmpty() {
        Attributes attributes = new Attributes();
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes result = settings.normalizeAttributes(attributes);
        assertEquals("", result.toString());
    }
}