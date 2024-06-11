import org.jsoup.parser.Attributes;
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
        String tag = settings.normalizeTag("TaG");
        assertEquals("tag", tag);
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        String attribute = settings.normalizeAttribute("ATTRIBUTE");
        assertEquals("ATTRIBUTE", attribute);
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("attr1", "value1");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("Value1", normalizedAttributes.get("Attr1"));
    }
}