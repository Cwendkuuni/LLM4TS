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
    public void testNormalizeTag_noPreserve() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("html", settings.normalizeTag("HTML"));
    }

    @Test
    public void testNormalizeTag_preserve() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("HTML", settings.normalizeTag("HTML"));
    }

    @Test
    public void testNormalizeAttribute_noPreserve() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("src", settings.normalizeAttribute("SRC"));
    }

    @Test
    public void testNormalizeAttribute_preserve() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("SRC", settings.normalizeAttribute("SRC"));
    }

    @Test
    public void testNormalizeAttributes_noPreserve() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNull(normalizedAttributes.get("HREF"));
    }

    @Test
    public void testNormalizeAttributes_preserve() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "example.com");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertNotNull(normalizedAttributes.get("HREF"));
    }
}