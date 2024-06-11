import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Assert.assertTrue(parseSettings.preserveTagCase());

        parseSettings = new ParseSettings(false, false);
        Assert.assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Assert.assertTrue(parseSettings.preserveAttributeCase());

        parseSettings = new ParseSettings(false, false);
        Assert.assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);

        // Typical case: tag name without case preservation
        String name = parseSettings.normalizeTag("div");
        Assert.assertEquals("div", name);

        // Edge case: tag name with case preservation
        name = parseSettings.normalizeTag("DIV");
        Assert.assertEquals("DIV", name);

        // Error case: tag name with leading and trailing spaces
        name = parseSettings.normalizeTag("  div  ");
        Assert.assertEquals("div", name);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);

        // Typical case: attribute name without case preservation
        String name = parseSettings.normalizeAttribute("class");
        Assert.assertEquals("class", name);

        // Edge case: attribute name with case preservation
        name = parseSettings.normalizeAttribute("CLASS");
        Assert.assertEquals("CLASS", name);

        // Error case: attribute name with leading and trailing spaces
        name = parseSettings.normalizeAttribute("  class  ");
        Assert.assertEquals("class", name);
    }

    @Test
    public void testNormalizeAttributes() {
        // Typical case: attributes with case preservation
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Class", "test");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertEquals(attributes, normalizedAttributes);

        // Edge case: attributes without case preservation
        parseSettings = new ParseSettings(false, false);
        attributes.put("Class", "test");
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertEquals(attributes, normalizedAttributes);

        // Error case: attributes without case preservation but with case-sensitive keys
        parseSettings = new ParseSettings(false, false);
        attributes.put("cLaSs", "test");
        normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertEquals(attributes, normalizedAttributes);
    }
}