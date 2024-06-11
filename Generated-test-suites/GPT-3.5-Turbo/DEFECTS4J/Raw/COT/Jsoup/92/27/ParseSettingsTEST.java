import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {

    private static ParseSettings htmlDefault;
    private static ParseSettings preserveCase;

    @BeforeClass
    public static void setUp() {
        htmlDefault = ParseSettings.htmlDefault;
        preserveCase = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(htmlDefault.preserveAttributeCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);

        assertEquals("div", settings.normalizeTag("DIV"));
        assertEquals("p", settings.normalizeTag("p "));
        assertEquals("a", settings.normalizeTag("A"));
    }

    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);

        assertEquals("id", settings.normalizeAttribute(" ID "));
        assertEquals("class", settings.normalizeAttribute("class"));
        assertEquals("href", settings.normalizeAttribute("HREF"));
    }

    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put(" ID ", "myId");
        attributes.put("class", "myClass");
        attributes.put("HREF", "http://example.com");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("myId", normalizedAttributes.get("id"));
        assertEquals("myClass", normalizedAttributes.get("class"));
        assertEquals("http://example.com", normalizedAttributes.get("href"));
    }

    @Test
    public void testNormalizeAttributes_preserveCase() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put(" ID ", "myId");
        attributes.put("class", "myClass");
        attributes.put("HREF", "http://example.com");

        Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

        assertEquals("myId", normalizedAttributes.get(" ID "));
        assertEquals("myClass", normalizedAttributes.get("class"));
        assertEquals("http://example.com", normalizedAttributes.get("HREF"));
    }

    @Test
    public void testParseSettingsConstructor() {
        ParseSettings settings = new ParseSettings(true, false);

        assertTrue(settings.preserveTagCase());
        assertFalse(settings.preserveAttributeCase());
    }
}


