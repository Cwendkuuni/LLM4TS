package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testHtmlDefaultConstructor() {
        ParseSettings parseSettings = new ParseSettings();
        assertFalse(parseSettings.preserveTagCase());
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCaseConstructor() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertTrue(parseSettings.preserveTagCase());
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testPreserveTagCaseMethod() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());

        ParseSettings parseSettings1 = new ParseSettings(false, false);
        assertFalse(parseSettings1.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCaseMethod() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());

        ParseSettings parseSettings1 = new ParseSettings(false, false);
        assertFalse(parseSettings1.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagMethod() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String name = "TAG";
        String expected = "tag";

        String normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "tag";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "Tag";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG ";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG\t";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG!";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "Tag!";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG!123";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG!12345";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG!123456";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);

        name = "TAG!1234567";
        expected = "tag";
        normalizedTag = parseSettings.normalizeTag(name);
        assertEquals(expected, normalizedTag);
    }

    @Test
    public void testNormalizeAttributeMethod() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String name = "ATTR";
        String expected = "attr";

        String normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "attr";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "Attr";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR ";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR\t";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR!";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "Attr!";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR!123";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR!12345";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR!123456";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);

        name = "ATTR!1234567";
        expected = "attr";
        normalizedAttribute = parseSettings.normalizeAttribute(name);
        assertEquals(expected, normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributesMethod() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        attributes.put("attr2", "value2");
        attributes.put("Attr1", "Value1");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr1", normalizedAttributes.keyAt(0));
        assertEquals("value1", normalizedAttributes.get(0));
        assertEquals("attr2", normalizedAttributes.keyAt(1));
        assertEquals("value2", normalizedAttributes.get(1));

        parseSettings = new ParseSettings(true, true);
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        assertEquals("attr1", normalizedAttributes.keyAt(0));
        assertEquals("value1", normalizedAttributes.get(0));
        assertEquals("Attr2", normalizedAttributes.keyAt(1));
        assertEquals("value2", normalizedAttributes.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAttributesMethodWithNullAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        parseSettings.normalizeAttributes(null);
    }
}