import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
        assertEquals("div", settings.normalizeTag("div"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("id", settings.normalizeAttribute("ID"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("Name", "Value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("Name"));
    }

    @Test
    public void testNormalizeAttributesNoPreserve() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("Name", "Value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("Name"));
    }
}