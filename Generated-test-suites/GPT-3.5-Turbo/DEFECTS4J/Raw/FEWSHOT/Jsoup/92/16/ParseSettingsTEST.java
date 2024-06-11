package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());

        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("example", settings.normalizeTag("Example"));

        settings = new ParseSettings(true, false);
        assertEquals("Example", settings.normalizeTag("Example"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute("CLASS"));

        settings = new ParseSettings(false, true);
        assertEquals("CLASS", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "example");
        attributes.put("Id", "123");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("class", attributes.getKey("CLASS"));
        assertEquals("id", attributes.getKey("Id"));

        settings = new ParseSettings(false, true);
        attributes = new Attributes();
        attributes.put("CLASS", "example");
        attributes.put("Id", "123");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("CLASS", attributes.getKey("CLASS"));
        assertEquals("Id", attributes.getKey("Id"));
    }
}