import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {
   
    @Test
    public void testPreserveTagCase() {
        ParseSettings ps = new ParseSettings(true, false);
        assertTrue(ps.preserveTagCase());
        
        ps = new ParseSettings(false, false);
        assertFalse(ps.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings ps = new ParseSettings(true, true);
        assertTrue(ps.preserveAttributeCase());
        
        ps = new ParseSettings(false, true);
        assertFalse(ps.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings ps = new ParseSettings(true, false);
        assertEquals("div", ps.normalizeTag("div"));
        
        ps = new ParseSettings(false, false);
        assertEquals("div", ps.normalizeTag("DIV"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings ps = new ParseSettings(true, true);
        assertEquals("src", ps.normalizeAttribute("src"));
        
        ps = new ParseSettings(false, true);
        assertEquals("src", ps.normalizeAttribute("SRC"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings ps = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        
        assertEquals(attributes, ps.normalizeAttributes(attributes));
        
        ps = new ParseSettings(false, true);
        assertEquals(attributes, ps.normalizeAttributes(attributes));
    }
}
