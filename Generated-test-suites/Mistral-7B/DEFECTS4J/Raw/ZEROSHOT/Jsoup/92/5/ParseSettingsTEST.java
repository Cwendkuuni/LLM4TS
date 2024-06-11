package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testParseSettingsHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        String tagName = "TAG";
        String attributeName = "ATTRIBUTE";
        String expectedTagName = "tag";
        String expectedAttributeName = "attribute";

        assertEquals("htmlDefault should have tag name lowercased", expectedTagName, settings.normalizeTag(tagName));
        assertEquals("htmlDefault should have attribute name lowercased", expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("AttributeName", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeName = normalizedAttributes.get("AttributeName").getValue();
        assertEquals("htmlDefault should have attribute name lowercased in normalizedAttributes", expectedAttributeName, normalizedAttributeName);
    }

    @Test
    public void testParseSettingsPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        String tagName = "TAG";
        String attributeName = "ATTRIBUTE";
        String expectedTagName = "TAG";
        String expectedAttributeName = "ATTRIBUTE";

        assertEquals("preserveCase should have tag name unchanged", expectedTagName, settings.normalizeTag(tagName));
        assertEquals("preserveCase should have attribute name unchanged", expectedAttributeName, settings.normalizeAttribute(attributeName));

        Attributes attributes = new Attributes();
        attributes.put("AttributeName", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String normalizedAttributeName = normalizedAttributes.get("AttributeName").getValue();
        assertEquals("preserveCase should have attribute name unchanged in normalizedAttributes", expectedAttributeName, normalizedAttributeName);
    }

    @Test
    public void testParseSettingsNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "TAG";
        String expectedTagName = "TAG";

        assertEquals("New ParseSettings instance with tag case preservation should have tag name unchanged", expectedTagName, settings.normalizeTag(tagName));
    }

    @Test
    public void testParseSettingsNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "ATTRIBUTE";
        String expectedAttributeName = "ATTRIBUTE";

        assertEquals("New ParseSettings instance with attribute case preservation should have attribute name unchanged", expectedAttributeName, settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testParseSettingsNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("AttributeName1", "value1");
        attributes.put("AttributeName2", "value2");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        String attributeName1 = normalizedAttributes.get("AttributeName1").getValue();
        String attributeName2 = normalizedAttributes.get("AttributeName2").getValue();

        assertEquals("ParseSettings instance with both tag and attribute case lowercasing should have normalized attribute names lowercased", "attributeName1", attributeName1);
        assertEquals("ParseSettings instance with both tag and attribute case lowercasing should have normalized attribute names lowercased", "attributeName2", attributeName2);
    }
}