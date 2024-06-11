
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertEquals("html", parseSettings.normalizeTag("Html"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertEquals("class", parseSettings.normalizeAttribute("Class"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Class", "value");
        parseSettings.normalizeAttributes(attributes);
        assertTrue(attributes.hasKey("class") && attributes.get("class").equals("value"));
    }
    
}
