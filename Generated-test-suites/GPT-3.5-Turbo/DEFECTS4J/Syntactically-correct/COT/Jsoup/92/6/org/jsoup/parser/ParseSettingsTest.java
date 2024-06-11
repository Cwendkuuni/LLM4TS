import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.Attributes;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(true, settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("TAG", settings.normalizeTag(" Tag "));
        
        settings = new ParseSettings(false, false);
        assertEquals("tag", settings.normalizeTag(" Tag "));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("attribute", settings.normalizeAttribute(" Attribute "));
        
        settings = new ParseSettings(false, false);
        assertEquals("attribute", settings.normalizeAttribute(" Attribute "));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put(" Attribute1 ", "Value1");
        attributes.put(" Attribute2 ", "Value2");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        
        assertEquals("attribute1", normalizedAttributes.getKey(" Attribute1 "));
        assertEquals("attribute2", normalizedAttributes.getKey(" Attribute2 "));
    }
}