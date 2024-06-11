import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class ParseSettingsTest {

    // Test case for preserveTagCase() method
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Assert.assertFalse(parseSettings.preserveTagCase());

        parseSettings = new ParseSettings(true, false);
        Assert.assertTrue(parseSettings.preserveTagCase());
    }

    // Test case for preserveAttributeCase() method
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Assert.assertFalse(parseSettings.preserveAttributeCase());

        parseSettings = new ParseSettings(false, true);
        Assert.assertTrue(parseSettings.preserveAttributeCase());
    }

    // Test case for normalizeTag() method - typical use case
    @Test
    public void testNormalizeTag_typical() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "Div";
        String result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("div", result);
    }

    // Test case for normalizeTag() method - edge case (empty tag name)
    @Test
    public void testNormalizeTag_empty() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        String tagName = "";
        String result = parseSettings.normalizeTag(tagName);
        Assert.assertEquals("", result);
    }

    // Test case for normalizeAttribute() method - typical use case
    @Test
    public void testNormalizeAttribute_typical() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String attributeName = "hREF";
        String result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("hREF", result);
    }

    // Test case for normalizeAttribute() method - edge case (attribute name with leading/trailing spaces)
    @Test
    public void testNormalizeAttribute_spaces() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "  Src  ";
        String result = parseSettings.normalizeAttribute(attributeName);
        Assert.assertEquals("  src  ", result);
    }

    // Test case for normalizeAttributes() method - typical use case
    @Test
    public void testNormalizeAttributes_typical() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "main");
        attributes.put("HREF", "https://example.com");
        Attributes result = parseSettings.normalizeAttributes(attributes);

        Assert.assertEquals("main", result.getIgnoreCase("id"));
        Assert.assertEquals("https://example.com", result.getIgnoreCase("HREF"));
    }

    // Test case for normalizeAttributes() method - error scenario (null attributes)
    @Test(expected = NullPointerException.class)
    public void testNormalizeAttributes_null() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        parseSettings.normalizeAttributes(null);
    }
}