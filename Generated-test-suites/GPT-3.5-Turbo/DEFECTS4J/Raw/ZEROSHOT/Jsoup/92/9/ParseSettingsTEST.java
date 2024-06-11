import org.junit.*;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        String name = "TEST";
        assertEquals("test", settings.normalizeTag(name));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        String name = "ATTRIBUTE";
        assertEquals("attribute", settings.normalizeAttribute(name));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("Key", "Value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertFalse(normalizedAttributes.hasKey("Key"));
        assertTrue(normalizedAttributes.hasKey("key"));
    }
}