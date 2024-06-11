import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertFalse(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertFalse(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("tag", parseSettings.normalizeTag("TAG"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("attribute", parseSettings.normalizeAttribute("ATTRIBUTE"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals(attributes, normalizedAttributes);
    }
}