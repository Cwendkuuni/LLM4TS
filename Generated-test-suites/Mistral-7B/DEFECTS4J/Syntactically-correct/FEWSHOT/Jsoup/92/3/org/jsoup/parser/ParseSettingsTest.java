package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertEquals(false, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tag = "HTML";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("html", normalizedTag);

        String attribute = "CLASS";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("class", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("class"));
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertEquals(true, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tag = "HTML";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("HTML", normalizedTag);

        String attribute = "CLASS";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("CLASS", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());

        String tag = "HTML";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("HTML", normalizedTag);

        String attribute = "CLASS";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("class", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(false, settings.preserveTagCase());
        assertEquals(true, settings.preserveAttributeCase());

        String tag = "HTML";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("html", normalizedTag);

        String attribute = "CLASS";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("CLASS", normalizedAttribute);

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testNullInput() {
        ParseSettings settings = new ParseSettings(false, false);
        assertNull(settings.normalizeTag(null));
        assertNull(settings.normalizeAttribute(null));
        assertNull(settings.normalizeAttributes(null));
    }
}