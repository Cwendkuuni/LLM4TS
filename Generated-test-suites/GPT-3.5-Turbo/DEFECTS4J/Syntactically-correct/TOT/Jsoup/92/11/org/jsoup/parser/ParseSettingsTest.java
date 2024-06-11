import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

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
        ParseSettings parseSettings = new ParseSettings(true, false);
        
        String tagName = "Div";
        String normalizedTagName = parseSettings.normalizeTag(tagName);
        
        Assert.assertEquals("div", normalizedTagName);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        
        String attributeName = "ID";
        String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
        
        Assert.assertEquals("id", normalizedAttributeName);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "myId");
        attributes.put("ClAss", "myClass");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        Assert.assertEquals("id", normalizedAttributes.get("ID"));
        Assert.assertEquals("class", normalizedAttributes.get("ClASS"));
    }
}