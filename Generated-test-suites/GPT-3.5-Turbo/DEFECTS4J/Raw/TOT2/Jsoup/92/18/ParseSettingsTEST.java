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
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("div", parseSettings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("class", parseSettings.normalizeAttribute("CLASS"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("class", normalizedAttributes.asList().get(0).getKey());
        assertEquals("test", normalizedAttributes.asList().get(0).getValue());
    }
}