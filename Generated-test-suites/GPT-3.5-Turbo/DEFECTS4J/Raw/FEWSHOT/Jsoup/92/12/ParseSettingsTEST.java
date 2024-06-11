package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    private ParseSettings htmlDefault;
    private ParseSettings preserveCase;

    @Before
    public void setUp() {
        htmlDefault = new ParseSettings(false, false);
        preserveCase = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
        ParseSettings settings = new ParseSettings(false, true);
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("html", htmlDefault.normalizeTag("HTML"));
        assertEquals("body", htmlDefault.normalizeTag(" Body "));
        assertEquals("p", preserveCase.normalizeTag("p"));
        assertEquals("div", preserveCase.normalizeTag("DIV"));
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("span", settings.normalizeTag("SPAN"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("charset", htmlDefault.normalizeAttribute("CHARSET"));
        assertEquals("http-equiv", htmlDefault.normalizeAttribute(" http-equiv "));
        assertEquals("class", preserveCase.normalizeAttribute("class"));
        assertEquals("src", preserveCase.normalizeAttribute("SRC"));
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("href", settings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("CLASS", "foo");
        attributes.put("SRC", "bar");
        Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.getKey("CLASS"));
        assertEquals("src", normalizedAttributes.getKey("SRC"));
        attributes = new Attributes();
        attributes.put(" HREF ", "http://example.com");
        normalizedAttributes = preserveCase.normalizeAttributes(attributes);
        assertEquals(" HREF ", normalizedAttributes.getKey(" HREF "));
    }
}