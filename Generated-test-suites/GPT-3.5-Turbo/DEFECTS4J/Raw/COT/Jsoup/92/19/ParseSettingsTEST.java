
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
        assertEquals("h1", settings.normalizeTag("H1"));
        assertEquals("div", settings.normalizeTag(" div "));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute(" Class "));
        assertEquals("data-src", settings.normalizeAttribute("DATA-SRC"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("Class", "className");
        attributes.put("HREF", "https://www.example.com");
        
        ParseSettings settings = new ParseSettings(false, false);
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertTrue(normalizedAttributes.hasKey("class"));
        assertEquals("className", normalizedAttributes.get("class"));
        
        assertTrue(normalizedAttributes.hasKey("href"));
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
    }
}