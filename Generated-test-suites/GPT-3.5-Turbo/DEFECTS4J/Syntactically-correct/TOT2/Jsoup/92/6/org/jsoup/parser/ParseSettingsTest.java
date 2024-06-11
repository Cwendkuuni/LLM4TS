import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
        
        settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
        
        settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("div", settings.normalizeTag("DIV"));
        
        settings = new ParseSettings(false, false);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("id", settings.normalizeAttribute("id"));
        
        settings = new ParseSettings(false, true);
        assertEquals("id", settings.normalizeAttribute("ID"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("123", normalizedAttributes.get("id"));
    }
}