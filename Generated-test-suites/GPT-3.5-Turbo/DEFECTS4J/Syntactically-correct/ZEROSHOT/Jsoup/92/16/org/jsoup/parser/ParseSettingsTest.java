import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        boolean result = settings.preserveTagCase();
        assertTrue(result);
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        boolean result = settings.preserveAttributeCase();
        assertTrue(result);
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tag = "<html>";
        String normalizedTag = settings.normalizeTag(tag);
        assertEquals("<html>", normalizedTag);
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attribute = "HrEF";
        String normalizedAttribute = settings.normalizeAttribute(attribute);
        assertEquals("href", normalizedAttribute);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attrs = new Attributes();
        attrs.put("HrEF", "https://www.example.com");
        Attributes normalizedAttrs = settings.normalizeAttributes(attrs);
        assertEquals("https://www.example.com", normalizedAttrs.get("https://www.example.com"));
    }
}