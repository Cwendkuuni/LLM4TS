import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());
        String tagName = settings.normalizeTag("TAG");
        Assert.assertEquals("tag", tagName);
        String attributeName = settings.normalizeAttribute("ATTRIBUTE");
        Assert.assertEquals("attribute", attributeName);
        Attributes attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        Attributes normalizedAttributes = new ParseSettings(ParseSettings.htmlDefault).normalizeAttributes(attributes);
        Assert.assertFalse(normalizedAttributes.getClass().isInstance(attributes));
        Assert.assertEquals("attribute", normalizedAttributes.get("attribute").getValue());
    }

    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());
        String tagName = settings.normalizeTag("TAG");
        Assert.assertEquals("TAG", tagName);
        String attributeName = settings.normalizeAttribute("ATTRIBUTE");
        Assert.assertEquals("ATTRIBUTE", attributeName);
        Attributes attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        Attributes normalizedAttributes = new ParseSettings(settings).normalizeAttributes(attributes);
        Assert.assertTrue(normalizedAttributes.getClass().isInstance(attributes));
        Assert.assertEquals("ATTRIBUTE", normalizedAttributes.get("ATTRIBUTE").getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        new ParseSettings(true, false, "invalid");
    }

    @Test
    public void testNormalizeTagNull() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = settings.normalizeTag(null);
        Assert.assertNull(tagName);
    }

    @Test
    public void testNormalizeTagEmpty() {
        ParseSettings settings = new ParseSettings(true, true);
        String tagName = settings.normalizeTag("");
        Assert.assertNull(tagName);
    }

    @Test
    public void testNormalizeAttributeNull() {
        ParseSettings settings = new ParseSettings(true, true);
        String attributeName = settings.normalizeAttribute(null);
        Assert.assertNull(attributeName);
    }

    @Test
    public void testNormalizeAttributeEmpty() {
        ParseSettings settings = new ParseSettings(true, true);
        String attributeName = settings.normalizeAttribute("");
        Assert.assertNull(attributeName);
    }

    @Test
    public void testNormalizeAttributesNull() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = null;
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertNull(normalizedAttributes);
    }

    @Test
    public void testNormalizeAttributesEmpty() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        Assert.assertEquals(0, normalizedAttributes.size());
    }
}