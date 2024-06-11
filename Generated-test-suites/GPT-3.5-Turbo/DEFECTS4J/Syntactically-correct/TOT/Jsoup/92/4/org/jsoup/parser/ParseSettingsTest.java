import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        // Typical use case - preserve tag case is true
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertTrue(parseSettings.preserveTagCase());

        // Typical use case - preserve tag case is false
        ParseSettings parseSettings2 = new ParseSettings(false, true);
        assertFalse(parseSettings2.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        // Typical use case - preserve attribute case is true
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertTrue(parseSettings.preserveAttributeCase());

        // Typical use case - preserve attribute case is false
        ParseSettings parseSettings2 = new ParseSettings(true, false);
        assertFalse(parseSettings2.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        // Typical use case - tag case is preserved
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("p", parseSettings.normalizeTag("p"));

        // Typical use case - tag case is not preserved
        ParseSettings parseSettings2 = new ParseSettings(false, true);
        assertEquals("p", parseSettings2.normalizeTag("P"));
        
        // Edge case - empty tag name
        assertEquals("", parseSettings.normalizeTag(""));
        
        // Edge case - tag name with leading/trailing spaces
        assertEquals("p", parseSettings.normalizeTag("  p  "));
        
        // Error scenario - null tag name
        try {
            parseSettings.normalizeTag(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }
    
    @Test
    public void testNormalizeAttribute() {
        // Typical use case - attribute case is preserved
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("class", parseSettings.normalizeAttribute("class"));

        // Typical use case - attribute case is not preserved
        ParseSettings parseSettings2 = new ParseSettings(true, false);
        assertEquals("class", parseSettings2.normalizeAttribute("CLASS"));
        
        // Edge case - empty attribute name
        assertEquals("", parseSettings.normalizeAttribute(""));
        
        // Edge case - attribute name with leading/trailing spaces
        assertEquals("class", parseSettings.normalizeAttribute("  class  "));
        
        // Error scenario - null attribute name
        try {
            parseSettings.normalizeAttribute(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }
    
    @Test
    public void testNormalizeAttributes() {
        // Typical use case - attribute case is not preserved
        ParseSettings parseSettings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("href", "https://www.example.com");
        attributes.put("class", "header");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("https://www.example.com", normalizedAttributes.get("href"));
        assertEquals("header", normalizedAttributes.get("class"));
        
        // Typical use case - attribute case is preserved
        ParseSettings parseSettings2 = new ParseSettings(false, true);
        Attributes attributes2 = new Attributes();
        attributes2.put("HREF", "https://www.example.com");
        attributes2.put("CLASS", "header");
        Attributes normalizedAttributes2 = parseSettings2.normalizeAttributes(attributes2);
        assertEquals("https://www.example.com", normalizedAttributes2.get("HREF"));
        assertEquals("header", normalizedAttributes2.get("CLASS"));
        
        // Edge case - attributes with no entries
        Attributes attributes3 = new Attributes();
        Attributes normalizedAttributes3 = parseSettings2.normalizeAttributes(attributes3);
        assertEquals(0, normalizedAttributes3.size());

        // Error scenario - null attributes
        try {
            parseSettings.normalizeAttributes(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }
}