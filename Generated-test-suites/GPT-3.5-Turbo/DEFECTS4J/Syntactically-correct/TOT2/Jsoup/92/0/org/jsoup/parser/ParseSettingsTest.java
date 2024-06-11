import org.jsoup.nodes.Attributes;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {

    private static ParseSettings htmlDefaultSettings;
    private static ParseSettings preserveCaseSettings;

    @BeforeClass
    public static void setUp() {
        htmlDefaultSettings = ParseSettings.htmlDefault;
        preserveCaseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertEquals(false, htmlDefaultSettings.preserveTagCase());
        assertEquals(true, preserveCaseSettings.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertEquals(false, htmlDefaultSettings.preserveAttributeCase());
        assertEquals(true, preserveCaseSettings.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("test", settings.normalizeTag("   TeSt   "));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("test", settings.normalizeAttribute("   TeSt   "));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        attributes.put("TestAttr", "value");
        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
        assertEquals("value", normalizedAttributes.get("testattr"));
    }
}