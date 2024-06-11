
package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.*;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    private ParseSettings parseSettings;

    @Before
    public void setup() {
        parseSettings = new ParseSettings(false, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        String tagName = "DIV";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        assertEquals("div", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        String attributeName = "HREF";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        assertEquals("href", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        attributes.put("target", "_blank");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertNotNull(normalizedAttributes);
        assertEquals("href", normalizedAttributes.get("href"));
        assertEquals("_blank", normalizedAttributes.get("target"));
    }

    @Test
    public void testStaticFields() {
        assertNotNull(ParseSettings.htmlDefault);
        assertNotNull(ParseSettings.preserveCase);
    }

}
