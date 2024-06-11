import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

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
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveAttributeCase());

        settings = new ParseSettings(true, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        String tagName = "h1";
        assertEquals("h1", settings.normalizeTag(tagName));

        tagName = "H1";
        assertEquals("H1", settings.normalizeTag(tagName));

        tagName = "   H1   ";
        assertEquals("H1", settings.normalizeTag(tagName));

        settings = new ParseSettings(false, false);

        tagName = "h1";
        assertEquals("h1", settings.normalizeTag(tagName));

        tagName = "H1";
        assertEquals("h1", settings.normalizeTag(tagName));

        tagName = "   H1   ";
        assertEquals("h1", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);

        String attributeName = "src";
        assertEquals("src", settings.normalizeAttribute(attributeName));

        attributeName = "SRC";
        assertEquals("SRC", settings.normalizeAttribute(attributeName));

        attributeName = "   SRC   ";
        assertEquals("SRC", settings.normalizeAttribute(attributeName));

        settings = new ParseSettings(false, true);

        attributeName = "src";
        assertEquals("src", settings.normalizeAttribute(attributeName));

        attributeName = "SRC";
        assertEquals("src", settings.normalizeAttribute(attributeName));

        attributeName = "   SRC   ";
        assertEquals("src", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);

        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("SRC", "https://www.example.com");
        attributes.put("title", "Example");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("https://www.example.com", normalizedAttributes.get("SRC"));
        assertEquals("Example", normalizedAttributes.get("title"));

        settings = new ParseSettings(false, true);

        attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("SRC", "https://www.example.com");
        attributes.put("title", "Example");

        normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("https://www.example.com", normalizedAttributes.get("src"));
        assertEquals("Example", normalizedAttributes.get("title"));
    }
}