
package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        assertEquals("div", settings.normalizeTag("DIV"));
        assertEquals("p", settings.normalizeTag("p"));
        assertEquals("button", settings.normalizeTag("<button>"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("name", settings.normalizeAttribute("NAME"));       
        assertEquals("data-value", settings.normalizeAttribute("   data-value   "));
        assertEquals("href", settings.normalizeAttribute("<a href=\"https://www.example.com\">"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("ATTR", "value");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("ATTR", normalizedAttributes.asList().get(0).getKey());
        assertEquals("value", normalizedAttributes.asList().get(0).getValue());
    }

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        assertFalse(settings.preserveTagCase());
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
    }

}

