package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);

        boolean result = parseSettings.preserveTagCase();

        Assert.assertTrue(result);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);

        boolean result = parseSettings.preserveAttributeCase();

        Assert.assertTrue(result);
    }

    @Test
    public void testNormalizeTagWithPreserveTagCaseFalse() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "p ";

        String result = parseSettings.normalizeTag(tagName);

        assertEquals("p", result);
    }

    @Test
    public void testNormalizeTagWithPreserveTagCaseTrue() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "p ";

        String result = parseSettings.normalizeTag(tagName);

        assertEquals("p ", result);
    }

    @Test
    public void testNormalizeAttributeWithPreserveAttributeCaseFalse() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = " Href ";

        String result = parseSettings.normalizeAttribute(attributeName);

        assertEquals("href", result);
    }

    @Test
    public void testNormalizeAttributeWithPreserveAttributeCaseTrue() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = " Href ";

        String result = parseSettings.normalizeAttribute(attributeName);

        assertEquals(" Href ", result);
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCaseFalse() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("CLASS", "container");

        Attributes result = parseSettings.normalizeAttributes(attributes);

        assertEquals(2, result.size());
        assertEquals("http://example.com", result.get("href"));
        assertEquals("container", result.get("class"));
    }

    @Test
    public void testNormalizeAttributesWithPreserveAttributeCaseTrue() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("CLASS", "container");

        Attributes result = parseSettings.normalizeAttributes(attributes);

        assertEquals(2, result.size());
        assertEquals("HREF", result.get("HREF"));
        assertEquals("CLASS", result.get("CLASS"));
    }

}