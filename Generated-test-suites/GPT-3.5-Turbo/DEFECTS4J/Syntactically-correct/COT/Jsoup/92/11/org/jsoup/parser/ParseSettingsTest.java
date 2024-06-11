import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

    // Test case for preserveTagCase() method
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    // Test case for preserveAttributeCase() method
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    // Test case for normalizeTag() method
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("DIV");
        assertEquals("DIV", tagName);
    }

    // Test case for normalizeAttribute() method
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attributeName = settings.normalizeAttribute("href");
        assertEquals("href", attributeName);
    }

    // Test case for normalizeAttributes() method
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("HREF", "https://www.example.com");
        attributes = settings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", attributes.get("href"));
    }

}