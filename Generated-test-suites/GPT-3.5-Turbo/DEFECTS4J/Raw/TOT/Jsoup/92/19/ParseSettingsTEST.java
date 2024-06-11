import org.junit.Test;
import org.junit.Assert;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        Assert.assertTrue(settings.preserveTagCase());

        String tag = "h1";
        Assert.assertEquals("h1", settings.normalizeTag(tag));

        tag = "H2";
        Assert.assertEquals("H2", settings.normalizeTag(tag));
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        Assert.assertTrue(settings.preserveAttributeCase());

        String attribute = "class";
        Assert.assertEquals("class", settings.normalizeAttribute(attribute));

        attribute = "ID";
        Assert.assertEquals("ID", settings.normalizeAttribute(attribute));
    }

    @Test
    public void testPreserveTagAndAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Assert.assertTrue(settings.preserveTagCase());
        Assert.assertTrue(settings.preserveAttributeCase());

        String tag = "h1";
        Assert.assertEquals("h1", settings.normalizeTag(tag));

        tag = "H2";
        Assert.assertEquals("H2", settings.normalizeTag(tag));

        String attribute = "class";
        Assert.assertEquals("class", settings.normalizeAttribute(attribute));

        attribute = "ID";
        Assert.assertEquals("ID", settings.normalizeAttribute(attribute));
    }

    @Test
    public void testPreserveNoCase() {
        ParseSettings settings = new ParseSettings(false, false);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tag = "h1";
        Assert.assertEquals("h1", settings.normalizeTag(tag));

        tag = "H2";
        Assert.assertEquals("h2", settings.normalizeTag(tag));

        String attribute = "class";
        Assert.assertEquals("class", settings.normalizeAttribute(attribute));

        attribute = "ID";
        Assert.assertEquals("id", settings.normalizeAttribute(attribute));
    }

    @Test
    public void testTrimming() {
        ParseSettings settings = new ParseSettings(false, false);
        Assert.assertFalse(settings.preserveTagCase());
        Assert.assertFalse(settings.preserveAttributeCase());

        String tag = "   h1   ";
        Assert.assertEquals("h1", settings.normalizeTag(tag));

        String attribute = "   class   ";
        Assert.assertEquals("class", settings.normalizeAttribute(attribute));
    }

    @Test
    public void testAttributesNormalization() {
        ParseSettings settings = new ParseSettings(false, false);
        Assert.assertFalse(settings.preserveAttributeCase());

        Attributes attributes = new Attributes();
        attributes.put("CLASS", "value");
        attributes.put("ID", "value");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        Assert.assertEquals("class", normalizedAttributes.getKey("class"));
        Assert.assertEquals("value", normalizedAttributes.getValue("class"));

        Assert.assertEquals("id", normalizedAttributes.getKey("id"));
        Assert.assertEquals("value", normalizedAttributes.getValue("id"));
    }

}