package org.jsoup.parser;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ParseSettingsTest {

    private ParseSettings parseSettings;

    @BeforeEach
    void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    void testNormalizeTag() {
        String tag = "div";
        assertEquals("div", parseSettings.normalizeTag(tag));

        tag = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tag));

        tag = "  Div ";
        assertEquals("div", parseSettings.normalizeTag(tag));

        tag = "";
        assertEquals("", parseSettings.normalizeTag(tag));
    }

    @Test
    void testNormalizeAttribute() {
        String attribute = "class";
        assertEquals("class", parseSettings.normalizeAttribute(attribute));

        attribute = "CLASS";
        assertEquals("class", parseSettings.normalizeAttribute(attribute));

        attribute = "  cLaSs ";
        assertEquals("class", parseSettings.normalizeAttribute(attribute));

        attribute = "";
        assertEquals("", parseSettings.normalizeAttribute(attribute));
    }

    @Test
    void testNormalizeAttributes() {
        Attributes originalAttributes = new Attributes();
        originalAttributes.put("class", "test");
        originalAttributes.put("ID", "123");
        originalAttributes.put("style", "font-size:12px");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(originalAttributes);

        assertEquals("class", normalizedAttributes.getKey(0));
        assertEquals("id", normalizedAttributes.getKey(1));
        assertEquals("style", normalizedAttributes.getKey(2));

        assertEquals("test", normalizedAttributes.getValue("class"));
        assertEquals("123", normalizedAttributes.getValue("id"));
        assertEquals("font-size:12px", normalizedAttributes.getValue("style"));
    }

    @Test
    void testHtmlDefault() {
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertFalse(htmlDefault.preserveTagCase());
        assertFalse(htmlDefault.preserveAttributeCase());
    }

    @Test
    void testPreserveCase() {
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertTrue(preserveCase.preserveTagCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }
}