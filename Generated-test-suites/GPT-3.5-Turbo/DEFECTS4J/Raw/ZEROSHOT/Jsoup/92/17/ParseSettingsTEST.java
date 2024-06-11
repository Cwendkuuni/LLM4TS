import org.junit.Assert;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagPreserveCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tag = "DIV";
        String normalizedTag = settings.normalizeTag(tag);
        Assert.assertEquals(tag, normalizedTag);
    }

    @Test
    public void testNormalizeTagLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String tag = "DIV";
        String normalizedTag = settings.normalizeTag(tag);
        Assert.assertEquals("div", normalizedTag);
    }

    @Test
    public void testNormalizeAttributePreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String attribute = "HREF";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        Assert.assertEquals(attribute, normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributeLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        String attribute = "HREF";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        Assert.assertEquals("href", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributesPreserveCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(attributes, normalizedAttributes);
    }

    @Test
    public void testNormalizeAttributesLowerCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("href", "https://www.example.com");
        Assert.assertEquals(expectedAttributes, normalizedAttributes);
    }
}