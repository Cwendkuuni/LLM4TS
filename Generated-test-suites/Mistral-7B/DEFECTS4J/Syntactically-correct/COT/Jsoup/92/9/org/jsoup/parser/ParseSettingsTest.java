package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    /**
     * Test constructor.
     */
    @Test
    public void testParseSettingsConstructor() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);
        assertNotNull(htmlDefault);
        assertNotNull(preserveCase);
    }

    /**
     * Test preserveTagCase method.
     */
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TestTag";
        String normalizedTagName = settings.normalizeTag(tagName);
        assertEquals(tagName, normalizedTagName);
    }

    /**
     * Test preserveAttributeCase method.
     */
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attrName = "TestAttr";
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals(attrName, normalizedAttrName);
    }

    /**
     * Test preserveTagCase and preserveAttributeCase methods.
     */
    @Test
    public void testPreserveTagAndAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = "TestTag";
        String attrName = "TestAttr";
        String normalizedTagName = settings.normalizeTag(tagName);
        String normalizedAttrName = settings.normalizeAttribute(attrName);
        assertEquals(tagName, normalizedTagName);
        assertEquals(attrName, normalizedAttrName);
    }

    /**
     * Test normalizeAttributes method with preserveAttributeCase = false.
     */
    @Test
    public void testNormalizeAttributesPreserveAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("TestAttr", "TestValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("testattr", normalizedAttributes.keyAt(0).toLowerCase());
        assertEquals("testvalue", normalizedAttributes.valueAt(0));
    }

    /**
     * Test normalizeAttributes method with preserveAttributeCase = true.
     */
    @Test
    public void testNormalizeAttributesPreserveAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("TestAttr", "TestValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("TestAttr", normalizedAttributes.keyAt(0));
        assertEquals("TestValue", normalizedAttributes.valueAt(0));
    }

    /**
     * Test normalizeAttributes method with preserveTagCase = true.
     */
    @Test
    public void testNormalizeAttributesPreserveTagCaseTrue() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("TestTag", "TestValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("TestTag", normalizedAttributes.keyAt(0));
        assertEquals("TestValue", normalizedAttributes.valueAt(0));
    }

}