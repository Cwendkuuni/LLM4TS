package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    private static ParseSettings parseSettings;

    @BeforeClass
    public static void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        String name = "exampleTag";
        String expected = "exampletag";
        assertEquals(expected, parseSettings.normalizeTag(name));
    }

    @Test
    public void testNormalizeAttribute() {
        String name = "exampleAttribute";
        String expected = "exampleattribute";
        assertEquals(expected, parseSettings.normalizeAttribute(name));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("attribute1", "value1");
        attributes.put("attribute2", "value2");

        Attributes expected = new Attributes();
        expected.put("attribute1", "value1");
        expected.put("attribute2", "value2");

        assertEquals(expected, parseSettings.normalizeAttributes(attributes));
    }

    // Additional test cases for edge cases and exceptions
    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeEmptyTag() {
        String name = "";
        String expected = "";
        assertEquals(expected, parseSettings.normalizeTag(name));
    }

    @Test
    public void testNormalizeEmptyAttribute() {
        String name = "";
        String expected = "";
        assertEquals(expected, parseSettings.normalizeAttribute(name));
    }

    // Add more test cases for edge cases and exceptions if necessary

    // Test starts and ends here
    @Test
    public void testEnd() {
        assertEquals(0, 0);
    }
}