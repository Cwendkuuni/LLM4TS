
import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnit4.class)
public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Assert.assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Assert.assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String tagName = "TAG";
        Assert.assertEquals("tag", parseSettings.normalizeTag(tagName));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        String attributeName = "ATTRIBUTE";
        Assert.assertEquals("attribute", parseSettings.normalizeAttribute(attributeName));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ATTRIBUTE", "value");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        Assert.assertEquals("value", normalizedAttributes.get("attribute"));
    }
    
}
