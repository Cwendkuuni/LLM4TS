
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertFalse(settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("tag", settings.normalizeTag(" TAG "));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute("ATTRIBUTE"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("key"));
    }
}
