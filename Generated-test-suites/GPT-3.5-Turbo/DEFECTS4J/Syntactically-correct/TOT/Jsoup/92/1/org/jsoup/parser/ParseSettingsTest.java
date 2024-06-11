import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);

        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);

        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);

        String tagName = "a";
        assertEquals("a", settings.normalizeTag(tagName));

        tagName = "A";
        assertEquals("a", settings.normalizeTag(tagName));

        tagName = "    A   ";
        assertEquals("a", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, false);

        String attributeName = "HREF";
        assertEquals("HREF", settings.normalizeAttribute(attributeName));

        attributeName = "href";
        assertEquals("href", settings.normalizeAttribute(attributeName));

        attributeName = "     HREF     ";
        assertEquals("HREF", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);

        Attributes attributes = new Attributes();
        attributes.put("HREF", "http://example.com");
        attributes.put("target", "_blank");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertFalse(normalizedAttributes.hasKey("HREF"));
        assertTrue(normalizedAttributes.hasKey("href"));
        assertFalse(normalizedAttributes.hasKey("target"));
        assertTrue(normalizedAttributes.hasKey("TARGET"));
    }
}