import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {
    // Basic test cases

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        boolean actual = settings.preserveTagCase();
        assertTrue(actual);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        boolean actual = settings.preserveAttributeCase();
        assertTrue(actual);
    }

    @Test
    public void testNewParseSettings() {
        ParseSettings settings = new ParseSettings(true, true);
        boolean preserveTagCase = settings.preserveTagCase();
        boolean preserveAttributeCase = settings.preserveAttributeCase();
        assertTrue(preserveTagCase);
        assertTrue(preserveAttributeCase);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag("TAG");
        assertEquals("TAG", actual);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String actual = settings.normalizeAttribute("attributeName");
        assertEquals("attributeName", actual);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attributeName", "AttributeValue");
        Attributes actual = settings.normalizeAttributes(attributes);
        assertEquals("attributeName", actual.get("attributeName"));
    }

    // Edge cases and exception handling

    @Test(expected = NullPointerException.class)
    public void testNormalizeTagWithNull() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewParseSettingsWithInvalidBooleanValue() {
        boolean tag = "invalid";
        ParseSettings settings = new ParseSettings(tag, true);
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributesWithNullAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = null;
        Attributes actual = settings.normalizeAttributes(attributes);
    }

    @Test
    public void testNormalizeTagWithEmptyString() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag("");
        assertEquals("", actual);
    }

    @Test
    public void testNormalizeTagWithUpperCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag("TAG");
        assertEquals("TAG", actual);
    }

    @Test
    public void testNormalizeTagWithMixedCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String actual = settings.normalizeTag("TAG123");
        assertEquals("TAG123", actual);
    }

    @Test
    public void testNormalizeAttributeWithEmptyString() {
        ParseSettings settings = new ParseSettings(false, true);
        String actual = settings.normalizeAttribute("");
        assertEquals("", actual);
    }

    @Test
    public void testNormalizeAttributeWithUpperCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String actual = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("ATTRIBUTE", actual);
    }

    @Test
    public void testNormalizeAttributeWithMixedCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String actual = settings.normalizeAttribute("ATTRIBUTE123");
        assertEquals("ATTRIBUTE123", actual);
    }

    @Test
    public void testNormalizeAttributesWithEmptyAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes actual = settings.normalizeAttributes(new Attributes());
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testNormalizeAttributesWithAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("attributeName", "AttributeValue");
        Attributes actual = settings.normalizeAttributes(attributes);
        assertEquals("attributeName", actual.get("attributeName"));
    }
}