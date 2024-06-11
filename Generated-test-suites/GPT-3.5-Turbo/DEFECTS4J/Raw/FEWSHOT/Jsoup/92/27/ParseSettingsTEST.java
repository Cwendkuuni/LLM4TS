package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("html", parseSettings.normalizeTag("HTML"));
        assertEquals("body", parseSettings.normalizeTag("BODY"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("src", parseSettings.normalizeAttribute("SRC"));
        assertEquals("href", parseSettings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("SRC", "http://example.com/image.jpg");
        attributes.put("HREF", "http://example.com/page.html");
        attributes.put("ALT", "Image");
        assertEquals(true, parseSettings.normalizeAttributes(attributes));
    }
}