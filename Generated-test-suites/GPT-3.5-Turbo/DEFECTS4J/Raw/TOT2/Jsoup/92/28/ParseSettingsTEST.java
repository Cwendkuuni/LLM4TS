import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String normalizedTag = parseSettings.normalizeTag("UnNORMALized-tag");
        assertEquals("Unnormalized-tag", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String normalizedAttribute = parseSettings.normalizeAttribute("UnNORMALized-attribute");
        assertEquals("unnormalized-attribute", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals("value1", normalizedAttributes.get("attr1"));
        assertEquals("value2", normalizedAttributes.get("attr2"));
    }
}