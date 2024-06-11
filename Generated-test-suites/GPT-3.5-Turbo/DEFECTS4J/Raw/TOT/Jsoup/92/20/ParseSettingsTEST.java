import org.jsoup.nodes.Attributes;
import org.junit.*;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag_preserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "DIV";
        assertEquals(tagName, parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTag_lowerCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "DIV";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute_preserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "HREF";
        assertEquals(attributeName, parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttribute_lowerCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "HREF";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }
  
    @Test
    public void testNormalizeTag_emptyTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "";
        assertEquals("", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute_emptyAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "";
        assertEquals("", parseSettings.normalizeAttribute(attributeName));
    }
  
    @Test
    public void testNormalizeTag_whitespaceTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "  DIV ";
        assertEquals("div", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute_whitespaceAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "  HREF ";
        assertEquals("href", parseSettings.normalizeAttribute(attributeName));
    }
  
    @Test
    public void testNormalizeTag_nullTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = null;
        assertNull(parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute_nullAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = null;
        assertNull(parseSettings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes_preserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        assertEquals(attributes, parseSettings.normalizeAttributes(attributes));
    }

    @Test
    public void testNormalizeAttributes_lowerCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("href", "https://www.example.com");
        assertEquals(expectedAttributes, parseSettings.normalizeAttributes(attributes));
    }

    @Test
    public void testNormalizeAttributes_emptyAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        assertEquals(attributes, parseSettings.normalizeAttributes(attributes));
    }
  
    @Test
    public void testNormalizeAttributes_nullAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = null;
        assertNull(parseSettings.normalizeAttributes(attributes));
    }

}