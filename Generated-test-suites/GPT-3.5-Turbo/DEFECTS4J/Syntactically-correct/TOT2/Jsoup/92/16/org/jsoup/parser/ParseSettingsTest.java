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
        assertEquals("div", parseSettings.normalizeTag("div"));
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("div", parseSettings.normalizeTag("   DIV   "));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("id", parseSettings.normalizeAttribute("id"));
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
        assertEquals("id", parseSettings.normalizeAttribute("   ID   "));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "value");
        attributes.put("ID", "value");

        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("class"));
        assertEquals("value", normalizedAttributes.get("id"));
    }
}