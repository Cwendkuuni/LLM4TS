import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("tag", parseSettings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertEquals("attribute", parseSettings.normalizeAttribute("ATTRIBUTE"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("Attribute", "Value");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("attribute"));
    }

}