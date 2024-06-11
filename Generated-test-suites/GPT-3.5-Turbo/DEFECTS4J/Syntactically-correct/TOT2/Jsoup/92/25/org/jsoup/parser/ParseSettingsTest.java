import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTagNoPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, true);
        String result = settings.normalizeTag("DIV");
        assertEquals("div", result);
    }

    @Test
    public void testNormalizeTagPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String result = settings.normalizeTag("DIV");
        assertEquals("DIV", result);
    }

    @Test
    public void testNormalizeAttributeNoPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String result = settings.normalizeAttribute("HREF");
        assertEquals("href", result);
    }

    @Test
    public void testNormalizeAttributePreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, true);
        String result = settings.normalizeAttribute("HREF");
        assertEquals("HREF", result);
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        Attributes result = settings.normalizeAttributes(attributes);
        assertFalse(result.hasKey("HREF"));
        assertTrue(result.hasKey("href"));
    }
}