import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testNotPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNotPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreservedCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "DIV";
        assertEquals("DIV", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeTagNotPreservedCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tagName = "DIV";
        assertEquals("div", settings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttributePreservedCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = "HREF";
        assertEquals("HREF", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeAttributeNotPreservedCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attributeName = "HREF";
        assertEquals("href", settings.normalizeAttribute(attributeName));
    }

    @Test
    public void testNormalizeTagEmptyName() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = "";
        assertEquals("", settings.normalizeTag(tagName));
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeTagNullName() {
        ParseSettings settings = new ParseSettings(true, false);
        settings.normalizeTag(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributeNullName() {
        ParseSettings settings = new ParseSettings(true, false);
        settings.normalizeAttribute(null);
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("HREF"));
    }

    @Test
    public void testNormalizeAttributesNotPreserveCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
    }
}