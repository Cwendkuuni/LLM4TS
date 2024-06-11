import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    ParseSettings settings;

    @Before
    public void setUp() {
        settings = new ParseSettings(true, false);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreserveTagCase() {
        String tag = "h1";
        String normalizedTag = settings.normalizeTag(tag);

        assertEquals(tag, normalizedTag);
    }

    @Test
    public void testNormalizeTagLowercaseTag() {
        String tag = "H1";
        String normalizedTag = settings.normalizeTag(tag);

        assertEquals("h1", normalizedTag);
    }

    @Test
    public void testNormalizeAttributePreserveAttributeCase() {
        String attribute = "href";
        String normalizedAttribute = settings.normalizeAttribute(attribute);

        assertEquals(attribute, normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributeLowercaseAttribute() {
        String attribute = "HREF";
        String normalizedAttribute = settings.normalizeAttribute(attribute);

        assertEquals("href", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributesPreserveAttributeCase() {
        Attributes attributes = new Attributes();
        attributes.put("href", "https://example.com");
        attributes.put("CLASS", "header");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("https://example.com", normalizedAttributes.get("href"));
        assertEquals("header", normalizedAttributes.get("CLASS"));
    }

    @Test
    public void testNormalizeAttributesLowercaseAttribute() {
        settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://example.com");
        attributes.put("class", "header");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("https://example.com", normalizedAttributes.get("href"));
        assertEquals("header", normalizedAttributes.get("class"));
    }
}