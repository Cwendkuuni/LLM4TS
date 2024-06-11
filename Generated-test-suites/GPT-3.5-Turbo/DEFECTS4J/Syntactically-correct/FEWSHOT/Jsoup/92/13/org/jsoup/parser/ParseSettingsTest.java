import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    private ParseSettings htmlDefaultSettings;
    private ParseSettings preserveCaseSettings;

    @Before
    public void setUp() {
        htmlDefaultSettings = ParseSettings.htmlDefault;
        preserveCaseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, htmlDefaultSettings.preserveTagCase());
        assertEquals(true, preserveCaseSettings.preserveTagCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("html", htmlDefaultSettings.normalizeTag("HTML"));
        assertEquals("body", preserveCaseSettings.normalizeTag("BODY"));
        assertEquals("div", htmlDefaultSettings.normalizeTag(" div "));
        assertEquals("span", preserveCaseSettings.normalizeTag("SPAN"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("src", htmlDefaultSettings.normalizeAttribute("SRC"));
        assertEquals("href", preserveCaseSettings.normalizeAttribute("HREF"));
        assertEquals("alt", htmlDefaultSettings.normalizeAttribute(" alt "));
        assertEquals("data-value", preserveCaseSettings.normalizeAttribute("Data-Value"));
    }

    @Test
    public void testNormalizeAttributes() {
        // Test case with preserveAttributeCase = false
        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("SRC", "http://example.com/image.png");
        Attributes normalizedAttributes = htmlDefaultSettings.normalizeAttributes(attributes);
        assertEquals("http://example.com", normalizedAttributes.getIgnoreCase("href"));
        assertEquals("http://example.com/image.png", normalizedAttributes.getIgnoreCase("src"));

        // Test case with preserveAttributeCase = true
        attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("SRC", "http://example.com/image.png");
        normalizedAttributes = preserveCaseSettings.normalizeAttributes(attributes);
        assertEquals("http://example.com", normalizedAttributes.getIgnoreCase("HREF"));
        assertEquals("http://example.com/image.png", normalizedAttributes.getIgnoreCase("SRC"));
    }
}