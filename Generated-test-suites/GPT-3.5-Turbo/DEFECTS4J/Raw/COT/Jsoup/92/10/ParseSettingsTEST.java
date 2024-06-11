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
        ParseSettings settings = new ParseSettings(true, false);
        assertFalse(settings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("TEST", normalizedAttributes.get("class"));
    }
  
    @Test
    public void testHtmlDefault() {
        assertTrue(ParseSettings.htmlDefault.preserveTagCase());
        assertFalse(ParseSettings.htmlDefault.preserveAttributeCase());
    }
  
    @Test
    public void testPreserveCase() {
        ParseSettings settings = ParseSettings.preserveCase;
        assertTrue(settings.preserveTagCase());
        assertTrue(settings.preserveAttributeCase());
    }

}