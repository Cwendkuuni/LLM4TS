package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "HTML";
        String expectedTagName = "html";
        String attributeName = "class";
        String expectedAttributeName = "class";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("TestAttribute", "TestValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeValue = normalizedAttributes.get("TestAttribute");
        assertEquals(expectedAttributeName, normalizedAttributeValue);
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "DIV";
        String expectedTagName = "DIV";
        String attributeName = "CLASS";
        String expectedAttributeName = "CLASS";

        assertEquals(expectedTagName, settings.normalizeTag(tagName));
        assertEquals(expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("TestAttribute", "TestValue");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeValue = normalizedAttributes.get("TestAttribute");
        assertEquals(expectedAttributeName, normalizedAttributeValue);
    }

    @Test(expected = AssertionError.class)
    public void testLowercaseTagNameWithPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String tagName = "DIV";
        settings.normalizeTag(tagName);
        fail("Expected AssertionError.tagName should not be lowercased.");
    }

    @Test(expected = AssertionError.class)
    public void testLowercaseAttributeNameWithPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String attributeName = "CLASS";
        settings.normalizeAttribute(attributeName);
        fail("Expected AssertionError.attributeName should not be lowercased.");
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Attributes attributes = new Attributes();
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("TestAttribute2", "TestValue2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        String normalizedAttribute1 = normalizedAttributes.get("TestAttribute1");
        assertEquals("TestAttribute1", normalizedAttribute1);
        String normalizedAttribute2 = normalizedAttributes.get("TestAttribute2");
        assertEquals("TestAttribute2", normalizedAttribute2);
    }

    @Test
    public void testNormalizeAttributesWithLowercaseAttributeNames() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Attributes attributes = new Attributes();
        attributes.put("TestAttribute1", "TestValue1");
        attributes.put("testattribute2", "TestValue2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals(2, normalizedAttributes.size());
        String normalizedAttribute1 = normalizedAttributes.get("TestAttribute1");
        assertEquals("TestAttribute1", normalizedAttribute1);
        String normalizedAttribute2 = normalizedAttributes.get("testattribute2");
        assertEquals("testattribute2", normalizedAttribute2);
    }
}