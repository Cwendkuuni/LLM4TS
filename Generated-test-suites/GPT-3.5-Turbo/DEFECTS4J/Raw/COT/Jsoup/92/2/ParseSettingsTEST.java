import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    // Test case for preserveTagCase() method
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
    }

    // Test case for constructor with tag and attribute parameters
    @Test
    public void testConstructor() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        assertEquals(false, settings.preserveAttributeCase());
    }

    // Test case for normalizeTag() method with preserveTagCase set to true
    @Test
    public void testNormalizeTagPreserveTagCaseTrue() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("div");
        assertEquals("div", tagName);
    }

    // Test case for normalizeTag() method with preserveTagCase set to false
    @Test
    public void testNormalizeTagPreserveTagCaseFalse() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = settings.normalizeTag("Div");
        assertEquals("div", tagName);
    }

    // Test case for normalizeAttribute() method with preserveAttributeCase set to true
    @Test
    public void testNormalizeAttributePreserveAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(true, true);
        String attributeName = settings.normalizeAttribute("HREF");
        assertEquals("HREF", attributeName);
    }

    // Test case for normalizeAttribute() method with preserveAttributeCase set to false
    @Test
    public void testNormalizeAttributePreserveAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        String attributeName = settings.normalizeAttribute("HREF");
        assertEquals("href", attributeName);
    }

    // Test case for normalizeAttributes() method with preserveAttributeCase set to true
    @Test
    public void testNormalizeAttributesPreserveAttributeCaseTrue() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://example.com");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals(1, attributes.size());
        assertEquals("https://example.com", attributes.get("HREF"));
    }

    // Test case for normalizeAttributes() method with preserveAttributeCase set to false
    @Test
    public void testNormalizeAttributesPreserveAttributeCaseFalse() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://example.com");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals(1, attributes.size());
        assertEquals("https://example.com", attributes.get("href"));
    }

}