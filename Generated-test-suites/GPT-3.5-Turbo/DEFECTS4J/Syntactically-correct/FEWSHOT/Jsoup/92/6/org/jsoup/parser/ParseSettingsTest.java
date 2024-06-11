import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals(true, parseSettings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals(true, parseSettings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag_preserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("div", parseSettings.normalizeTag("div"));
    }
    
    @Test
    public void testNormalizeTag_normalizeTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("div", parseSettings.normalizeTag("DIV"));
    }
    
    @Test
    public void testNormalizeAttribute_preserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("id", parseSettings.normalizeAttribute("id"));
    }
    
    @Test
    public void testNormalizeAttribute_normalizeAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
    }
    
    @Test
    public void testNormalizeAttributes_preserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        parseSettings.normalizeAttributes(attributes);
        assertEquals("test", attributes.get("id"));
    }
    
    @Test
    public void testNormalizeAttributes_normalizeAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "test");
        parseSettings.normalizeAttributes(attributes);
        assertEquals("test", attributes.get("id"));
    }
}