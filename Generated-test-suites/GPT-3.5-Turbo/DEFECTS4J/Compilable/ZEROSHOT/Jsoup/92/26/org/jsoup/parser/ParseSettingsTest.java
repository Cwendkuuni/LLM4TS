package org.jsoup.parser;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HREF";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        parseSettings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", attributes.get("href"));
    }

}