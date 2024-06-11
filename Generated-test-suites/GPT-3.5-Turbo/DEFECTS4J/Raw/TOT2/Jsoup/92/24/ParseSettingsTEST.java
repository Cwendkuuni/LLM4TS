import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {
    
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = new ParseSettings(true, true);
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(parseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertTrue(parseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("span", parseSettings.normalizeTag(" span "));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("class", parseSettings.normalizeAttribute("CLASS"));
        assertEquals("id", parseSettings.normalizeAttribute(" id "));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("ID", "123");
        attributes.put("CLASS", "example");
        
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("123", normalizedAttributes.get("id"));
        assertEquals("example", normalizedAttributes.get("class"));
    }
}