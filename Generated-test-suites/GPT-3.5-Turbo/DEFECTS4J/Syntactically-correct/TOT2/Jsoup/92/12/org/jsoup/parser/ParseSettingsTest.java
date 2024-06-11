import org.junit.Test;
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
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("id", settings.normalizeAttribute("Id"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        Attributes result = settings.normalizeAttributes(attributes);
        assertEquals("123", result.get("id"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullAttributeName() {
        ParseSettings settings = new ParseSettings(true, true);
        settings.normalizeAttribute(null);
    }
}