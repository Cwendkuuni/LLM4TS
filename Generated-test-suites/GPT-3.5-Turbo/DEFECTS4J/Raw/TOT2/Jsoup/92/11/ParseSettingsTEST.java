import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    private ParseSettings htmlDefault;
    private ParseSettings preserveCase;

    @Before
    public void setup() {
        htmlDefault = ParseSettings.htmlDefault;
        preserveCase = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, htmlDefault.preserveTagCase());
        assertEquals(true, preserveCase.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, htmlDefault.preserveAttributeCase());
        assertEquals(true, preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("div", settings.normalizeTag("DIV"));
        settings = new ParseSettings(true, false);
        assertEquals("DIV", settings.normalizeTag("DIV"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("id", settings.normalizeAttribute("ID"));
        settings = new ParseSettings(false, true);
        assertEquals("id", settings.normalizeAttribute("ID"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("ID", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("id"));
    }
}